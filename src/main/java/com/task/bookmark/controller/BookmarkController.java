package com.task.bookmark.controller;


import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bookmarks/")
public class BookmarkController {

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @GetMapping("getAllBookmarks")
    public List<Bookmark> getAllBookmarks(){
        return this.bookmarkRepository.findAll();
    }

    @PostMapping("addBookmark")
        public Bookmark createBookmark(@RequestBody Bookmark bookmark){
            return this.bookmarkRepository.save(bookmark);
        }

    @GetMapping("{id}")
    public ResponseEntity<Bookmark> getBookmarkById(@PathVariable(value = "id") Long bookmarkId)
          throws ResourceNotFoundException {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() ->new ResourceNotFoundException("Bookmark Not found for ID ::"+bookmarkId));

        return ResponseEntity.ok().body(bookmark);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Bookmark> updateBookmark(@PathVariable(value = "id") Long bookmarkId,
                                                 @RequestBody Bookmark bookmarkDetails) throws ResourceNotFoundException {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + bookmarkId));

        bookmark.setTitle(bookmarkDetails.getTitle());
        bookmark.setUrl(bookmarkDetails.getUrl());
        final Bookmark updatedBookmark = bookmarkRepository.save(bookmark);
        return ResponseEntity.ok(updatedBookmark);
    }

    @DeleteMapping("delete/{id}")
    public Map<String, Boolean> deleteBookmark(@PathVariable(value = "id") Long bookmarkId) throws ResourceNotFoundException {
        Bookmark bookmark = bookmarkRepository.findById(bookmarkId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + bookmarkId));

        bookmarkRepository.delete(bookmark);
        Map<String,Boolean> response =new HashMap<>();
        response.put("deleted",Boolean.TRUE);
        return response;
    }

}
