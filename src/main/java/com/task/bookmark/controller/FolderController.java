package com.task.bookmark.controller;


import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders/")
public class FolderController {


    @Autowired
    private FolderRepository folderRepository;

    @GetMapping("getAllFolders")
    public List<Folder> getAllFolders(){
        return this.folderRepository.findAll();
    }

    @PostMapping("addFolder")
    public Folder createFolder(@RequestBody Folder folder){
        return this.folderRepository.save(folder);
    }

    @GetMapping("folderName/{folderName}")
    public ResponseEntity<Folder> getBookmarkById(@PathVariable(value = "folderName") String folderName)
            throws ResourceNotFoundException {

        Folder folder = folderRepository.findByFolderName(folderName).orElseThrow(() ->new ResourceNotFoundException("Folder Not found for name ::"+folderName));

        return ResponseEntity.ok().body(folder);
    }


}
