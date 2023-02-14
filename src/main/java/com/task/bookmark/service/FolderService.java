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
public class FolderService {


    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;


    public List<Folder> getAllFolders() {
        return folderRepository.findAll();
    }

    public Folder saveFolder(Long userId, Folder folder) {
        User user = Optional.ofNullable(userId).map(targetUserId -> userRepository.findById(targetUserId).orElseThrow(() -> new ResourceNotFoundException("User Not found for ID ::" + userId))).orElse(null);
        folder.setUser(user);
        return folderRepository.save(folder);
    }

    public void deleteFolder(Long folderId) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder not found for this id :: " + folderId));
        folderRepository.delete(folder);
    }


    public Folder getFolderById(Long folderId) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId));
        return folder;
    }

    public Folder updateFolder(Long folderId, Folder folderDetails) throws ResourceNotFoundException {
        Folder folder = folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId));
        folder.setFolderName(folderDetails.getFolderName());
        return folderRepository.save(folder);
    }

    public List<Bookmark> getBookmarksByFolderId(Long folderId) throws ResourceNotFoundException {
        folderRepository.findById(folderId).orElseThrow(() -> new ResourceNotFoundException("Folder Not found for ID ::" + folderId));
        List<Bookmark> bookmarkList = bookmarkRepository.findByFolderId(folderId);
        return bookmarkList;
    }
}
