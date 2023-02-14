package com.task.bookmark.controller;


import com.task.bookmark.dto.BookmarkDTO;
import com.task.bookmark.dto.FolderDTO;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.service.BookmarkMapper;
import com.task.bookmark.service.FolderMapper;
import com.task.bookmark.service.FolderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/folders")
public class FolderController {

    @Autowired
    FolderMapper folderMapper;

    @Autowired
    BookmarkMapper bookmarkMapper;


    @Autowired
    private FolderService folderService;

    @GetMapping
    public List<FolderDTO> getAllFolders() {
        return folderMapper.toFolderDTOList(folderService.getAllFolders());
    }

    @PostMapping
    public ResponseEntity<FolderDTO> createFolder(@Valid @RequestBody FolderDTO folderdto) {
        Folder folderRequest = folderMapper.toFolder(folderdto); // Convert To Folder
        Folder folder = folderService.saveFolder(folderdto.getUserId(), folderRequest); // Send To Service Layer
        FolderDTO folderResponse = folderMapper.toFolderDTO(folder); // Convert back to FolderDTO
        return new ResponseEntity<>(folderResponse, HttpStatus.CREATED);
    }


    @GetMapping("/{folderId}")
    public ResponseEntity<FolderDTO> getFolderById(@PathVariable(value = "folderId") Long folderId) throws ResourceNotFoundException {
        return ResponseEntity.ok().body(folderMapper.toFolderDTO(folderService.getFolderById(folderId)));
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(@PathVariable(value = "folderId") Long folderId) throws ResourceNotFoundException {
        folderService.deleteFolder(folderId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{folderId}")
    public ResponseEntity<FolderDTO> updateFolder(@PathVariable(value = "folderId") Long folderId, @Valid @RequestBody FolderDTO folderDetails) throws ResourceNotFoundException {
        Folder folderRequest = folderMapper.toFolder(folderDetails); // Convert to Model
        Folder folder = folderService.updateFolder(folderId, folderRequest); // Send to Service Layer
        FolderDTO folderResponse = folderMapper.toFolderDTO(folder); // Convert to DTO
        return ResponseEntity.ok(folderResponse);
    }

    @GetMapping("/{folderId}/bookmarks")
    public ResponseEntity<List<BookmarkDTO>> getBookmarksByFolderId(@PathVariable(value = "folderId") Long folderId) throws ResourceNotFoundException {
        List<Bookmark> bookmarkList = folderService.getBookmarksByFolderId(folderId);// To service layer
        List<BookmarkDTO> bookmarkDTOList = bookmarkMapper.toBookmarkDTOList(bookmarkList);// Convert to DTO
        return ResponseEntity.ok(bookmarkDTOList);
    }

}
