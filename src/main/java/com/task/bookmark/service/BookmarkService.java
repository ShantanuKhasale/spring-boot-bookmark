package com.task.bookmark.service;


import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.BookmarkRepository;
import com.task.bookmark.repository.FolderRepository;
import com.task.bookmark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Bookmark> getAllBookmarks() {
        return bookmarkRepository.findAll();
    }

    public Bookmark saveBookmark(Long userId, Long folderId, Bookmark bookmark) throws ResourceNotFoundException {
        Folder folder = Optional.ofNullable(folderId).map(targetFolderId -> folderRepository.findById(targetFolderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId))).orElse(null);
        User user = Optional.ofNullable(userId).map(targetUserId -> userRepository.findById(targetUserId).orElseThrow(() -> new ResourceNotFoundException("User Not found for ID ::" + userId))).orElse(null);
        bookmark.setFolder(folder);
        bookmark.setUser(user);
        return bookmarkRepository.save(bookmark);
    }

    public Bookmark getBookmarkById(Long bookmarkId) throws ResourceNotFoundException {
        return bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new ResourceNotFoundException("Bookmark Not found for ID ::" + bookmarkId));
    }

    public Bookmark updateBookmark(Long bookmarkId, Long newFolderId, Bookmark bookmarkDetails) throws ResourceNotFoundException {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new ResourceNotFoundException("Bookmark Not found for ID ::" + bookmarkId));
        Folder folder = Optional.ofNullable(newFolderId).map(folderId -> folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + newFolderId))).orElse(null);
        bookmark.setUrl(bookmarkDetails.getUrl());
        bookmark.setTitle(bookmarkDetails.getTitle());
        bookmark.setFolder(folder);
        return bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long bookmarkId) throws ResourceNotFoundException {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new ResourceNotFoundException("Bookmark not found for this id :: " + bookmarkId));
        bookmarkRepository.delete(bookmark);
    }

}
