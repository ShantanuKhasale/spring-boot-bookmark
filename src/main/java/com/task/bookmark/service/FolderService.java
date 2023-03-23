package com.task.bookmark.service;

import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.BookmarkRepository;
import com.task.bookmark.repository.FolderRepository;
import com.task.bookmark.repository.UserRepository;
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


@Service
public class FolderService {


    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Cacheable(value = "folders", key = "@userService.getCurrentUser().getId()")
    public List<Folder> getAllFolders() {
        User user = userService.getCurrentUser();
        return folderRepository.findByUserId(user.getId());
    }


    @Caching(evict = {@CacheEvict(value = "folders", key = "@userService.getCurrentUser().getId()")}, put = {@CachePut(value = "folder", key = "#result.getId()")})
    public Folder saveFolder(Folder folder) {
        User user = userService.getCurrentUser();
        folder.setUser(user);
        return folderRepository.save(folder);
    }

    @Caching(evict = {@CacheEvict(value = "folder", key = "#folderId"), @CacheEvict(value = "folders", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "bookmarks", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "folder_bookmarks", key = "#folderId")})
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void deleteFolder(Long folderId) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not Found for ID :: " + folderId));
        folder.getBookmarks().forEach(bookmark -> redisTemplate.delete("bookmark::" + bookmark.getId())); // delete bookmarks from cache for related folder
        folderRepository.delete(folder);
    }

    @Cacheable(value = "folder", key = "#folderId")
    public Folder getFolderById(Long folderId) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId));
        return folder;
    }


    @Caching(evict = {@CacheEvict(value = "folders", key = "@userService.getCurrentUser().getId()"), @CacheEvict(value = "folder_bookmarks", key = "#folderId")}, put = {@CachePut(value = "folder", key = "#folderId")})
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public Folder updateFolder(Long folderId, Folder folderDetails) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId));
        folder.setFolderName(folderDetails.getFolderName());
        return folderRepository.save(folder);
    }

    @Cacheable(value = "folder_bookmarks", key = "#folderId")
    public List<Bookmark> getBookmarksByFolderId(Long folderId) throws ResourceNotFoundException {
        folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId));
        List<Bookmark> bookmarkList = bookmarkRepository.findByFolderId(folderId);
        return bookmarkList;
    }

}
