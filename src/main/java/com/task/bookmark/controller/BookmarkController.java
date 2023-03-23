package com.task.bookmark.controller;


import com.task.bookmark.dto.BookmarkDTO;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.CreateBookmark;
import com.task.bookmark.service.BookmarkMapper;
import com.task.bookmark.service.BookmarkService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/bookmarks")
@Validated
public class BookmarkController {

    @Autowired
    BookmarkMapper bookmarkMapper;
    @Autowired
    BookmarkService bookmarkService;

    @GetMapping
    public List<BookmarkDTO> getAllBookmarks() {
        return bookmarkMapper.toBookmarkDTOList(bookmarkService.getAllBookmarks());
    }

    @PostMapping
    public ResponseEntity<BookmarkDTO> createBookmark(@Valid @RequestBody BookmarkDTO bookmarkdto) throws ResourceNotFoundException {
        Bookmark bookmarkRequest = bookmarkMapper.toBookmark(bookmarkdto); // Convert To Bookmark
        Bookmark bookmark = bookmarkService.saveBookmark(bookmarkdto.getFolderId(), bookmarkRequest); // Send To Service Layer
        BookmarkDTO bookmarkResponse = bookmarkMapper.toBookmarkDTO(bookmark); // Convert back to BookmarkDTO
        return new ResponseEntity<>(bookmarkResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{bookmarkId}")
    public ResponseEntity<BookmarkDTO> getBookmarkById(@PathVariable(value = "bookmarkId") Long bookmarkId) throws ResourceNotFoundException {
        BookmarkDTO bookmarkResponse = bookmarkMapper.toBookmarkDTO(bookmarkService.getBookmarkById(bookmarkId));
        return ResponseEntity.ok().body(bookmarkResponse);
    }

    @PutMapping("/{bookmarkId}")
    public ResponseEntity<BookmarkDTO> updateBookmark(@PathVariable(value = "bookmarkId") Long bookmarkId, @Valid @RequestBody BookmarkDTO bookmarkdto) throws ResourceNotFoundException {
        Bookmark bookmarkRequest = bookmarkMapper.toBookmark(bookmarkdto); // Convert To Bookmark
        Bookmark bookmark = bookmarkService.updateBookmark(bookmarkId, bookmarkdto.getFolderId(), bookmarkRequest); // Send To Service Layer
        BookmarkDTO bookmarkResponse = bookmarkMapper.toBookmarkDTO(bookmark); // Convert back to BookmarkDTO
        return ResponseEntity.ok(bookmarkResponse);
    }

    @DeleteMapping("/{bookmarkId}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable(value = "bookmarkId") Long bookmarkId) throws ResourceNotFoundException {
        bookmarkService.deleteBookmark(bookmarkId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/batch")
    public ResponseEntity<List<BookmarkDTO>> createBookmarks(@RequestBody @Valid List<BookmarkDTO> bookmarkDTOList) {

        List<CreateBookmark> bookmarksRequest = bookmarkMapper.toBookmarkReferenceList(bookmarkDTOList); // Convert To BookmarkList
        List<Bookmark> bookmarks = bookmarkService.saveBookmarks(bookmarksRequest); // Send To Service Layer
        List<BookmarkDTO> bookmarksResponse = bookmarkMapper.toBookmarkDTOList(bookmarks); // Convert back to BookmarkDTOList

        return ResponseEntity.status(HttpStatus.CREATED).body(bookmarksResponse);
    }


}
