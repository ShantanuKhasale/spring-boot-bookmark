package com.task.bookmark.service;


import com.task.bookmark.exception.MultipleResourceNotFoundException;
import com.task.bookmark.exception.ResourceDetail;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.CreateBookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.BookmarkRepository;
import com.task.bookmark.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    @Cacheable(value = "bookmarks", key = "@userService.getCurrentUser().getId()")
    public List<Bookmark> getAllBookmarks() {
        User user = userService.getCurrentUser();
        System.out.println("Database Call made");
        List<Bookmark> bookmarks = bookmarkRepository.findByUserId(user.getId());
        return bookmarks;
    }


    @Caching(evict = {@CacheEvict(value = "folder", key = "#folderId"), @CacheEvict(value = "folders", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "folder_bookmarks", key = "#folderId"), @CacheEvict(value = "bookmarks", key = "@userService.getCurrentUser().getId()")}, put = {@CachePut(value = "bookmark", key = "#result.getId()")})
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Bookmark saveBookmark(Long folderId, Bookmark bookmark) throws ResourceNotFoundException {
        User user = userService.getCurrentUser();
        Folder folder = Optional.ofNullable(folderId).map(targetFolderId -> folderRepository.findById(targetFolderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId))).orElse(null);
        bookmark.setFolder(folder);
        bookmark.setUser(user);
        Optional.ofNullable(folder).ifPresent(f -> f.setTotalBookmarks(f.getTotalBookmarks() + 1L)); // increment counter
        return bookmarkRepository.save(bookmark);
    }


    @Cacheable(value = "bookmark", key = "#bookmarkId")
    public Bookmark getBookmarkById(Long bookmarkId) throws ResourceNotFoundException {
        System.out.println("database call made");
        return bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new ResourceNotFoundException("Bookmark Not found for ID ::" + bookmarkId));
    }


    @Caching(evict = {@CacheEvict(value = "folders", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "bookmarks", key = "@userService.getCurrentUser().getId()")}, put = {@CachePut(value = "bookmark", key = "#bookmarkId")})
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Bookmark updateBookmark(Long bookmarkId, Long newFolderId, Bookmark bookmarkDetails) throws ResourceNotFoundException {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new ResourceNotFoundException("Bookmark Not found for ID ::" + bookmarkId));
        Folder folder = Optional.ofNullable(newFolderId).map(folderId -> folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + newFolderId))).orElse(null);

        Optional.ofNullable(bookmark.getFolder()).filter(f -> !f.getId().equals(newFolderId)) // Check if Folder is Changed
                .ifPresent(f -> {
                    f.setTotalBookmarks(f.getTotalBookmarks() - 1L); // Decrement bookmark count for old folder
                    redisTemplate.delete("folder::" + f.getId()); // delete cache "folder" for old folderId if exist
                    redisTemplate.delete("folder_bookmarks::" + f.getId()); // delete cache "folder_bookmarks for old folderId if exist
                });

        Optional.ofNullable(folder).filter(f -> !f.getId().equals(bookmark.getFolder().getId()))  // Check if Folder is Changed
                .ifPresent(f -> {
                    f.setTotalBookmarks(f.getTotalBookmarks() + 1L); // Increment bookmark count for new folder
                    redisTemplate.delete("folder::" + folder.getId()); // delete cache for new folderId if exist
                    redisTemplate.delete("folder_bookmarks::" + folder.getId()); // delete cache for new folderId if exist
                });

        bookmark.setUrl(bookmarkDetails.getUrl());
        bookmark.setTitle(bookmarkDetails.getTitle());
        bookmark.setFolder(folder);
        return bookmarkRepository.save(bookmark);
    }


    @Caching(evict = {@CacheEvict(value = "folders", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "bookmarks", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "bookmark", key = "#bookmarkId")})
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteBookmark(Long bookmarkId) throws ResourceNotFoundException {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new ResourceNotFoundException("Bookmark not found for this id :: " + bookmarkId));
        Folder folder = Optional.ofNullable(bookmark.getFolder()).map(f -> folderRepository.findById(f.getId()).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + bookmark.getFolder().getId()))).orElse(null);
        Optional.ofNullable(folder).ifPresent(f -> {
            f.setTotalBookmarks(f.getTotalBookmarks() - 1L); // decrement counter
            redisTemplate.delete("folder::" + f.getId()); // delete cache for old folderId
            redisTemplate.delete("folder_bookmarks::" + f.getId()); // delete cache for old folderId
        });
        bookmarkRepository.delete(bookmark);
    }


    @Caching(evict = {@CacheEvict(value = "folders", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "bookmarks", key = "@userService.getCurrentUser().getId()")})
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public List<Bookmark> saveBookmarks(List<CreateBookmark> bookmarkList) throws ResourceNotFoundException {
        User user = userService.getCurrentUser();
        Set<Long> folderIds = bookmarkList.stream().map(CreateBookmark::getFolderId).collect(Collectors.toSet()); // get unique folder ids
        List<Folder> folders = folderRepository.findAllById(folderIds); // retrieve folders
        Map<Long, Folder> folderMap = folders.stream().collect(Collectors.toMap(Folder::getId, Function.identity())); // Map for folderId and Folder

        List<ResourceDetail> exceptions = IntStream.range(0, bookmarkList.size())       // Use IntStream to iterate over the indexes of the bookmarkList
                .filter(i -> !folderMap.containsKey(bookmarkList.get(i).getFolderId()))  // Filter the indexes where the folderMap does not contain the folderId
                .mapToObj(index -> {
                    Long folderId = bookmarkList.get(index).getFolderId();              // get folderId of missing folder
                    String errorMessage = "Folder Not Found for ID :: " + folderId;
                    return new ResourceDetail((long) index, errorMessage);
                }).collect(Collectors.toList());

        if (!exceptions.isEmpty())
            throw new MultipleResourceNotFoundException("Exception Occurred while saving bookmarks", exceptions); // If there are any exceptions, throw a MultipleResourceNotFoundException with the list of exceptions


        folderIds.forEach(id -> redisTemplate.delete("folder::" + id)); // To clear cache "folder" for given folderIds
        folderIds.forEach(id -> redisTemplate.delete("folder_bookmarks::" + id)); // To clear cache "folder_bookmarks" for given folderIds

        List<Bookmark> bookmarks = bookmarkList.stream()
                .map(createBookmark -> {
                    Bookmark bookmark = new Bookmark(createBookmark.getTitle(), createBookmark.getUrl());
                    bookmark.setUser(user);
                    Folder folder = folderMap.get(createBookmark.getFolderId()); // get folder from hashMap
                    folder.setTotalBookmarks(folder.getTotalBookmarks() + 1);    // increment count
                    bookmark.setFolder(folder);
                    return bookmark;
                }).collect(Collectors.toList());

        return bookmarkRepository.saveAll(bookmarks);
    }

}
