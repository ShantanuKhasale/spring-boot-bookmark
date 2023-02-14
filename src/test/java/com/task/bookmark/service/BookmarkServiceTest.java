package com.task.bookmark.service;


import com.task.bookmark.AbstractContainerBaseTest;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
public class BookmarkServiceTest extends AbstractContainerBaseTest {

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    FolderService folderService;

    @Test
    public void ShouldCreateBookmarkSuccessfully() {
        // given
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", null);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(null, null, bookmark);
        // then
        Assertions.assertEquals(bookmark, savedBookmark);
    }

    @Test
    public void ShouldGetBookmarkDetailsById() {
        // given
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", null);
        // when
        bookmarkService.saveBookmark(null, null, bookmark);
        // then
        Assertions.assertEquals(bookmark, bookmarkService.getBookmarkById(bookmark.getId()));
    }

    @Test
    void ShouldThrowExceptionForInvalidFolderId() {
        //given
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com");
        Long invalidFolderId = 100L;
        // then
        // create one more test case for invalid userId
        assertThrows(ResourceNotFoundException.class, () -> bookmarkService.saveBookmark(null, invalidFolderId, bookmark));
    }

    @Test
    void ShouldThrowExceptionForInvalidBookmarkId() {
        //given
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com");
        Long invalidBookmarkId = 100L;
        //when
        bookmarkService.saveBookmark(null, null, bookmark);
        //then
        assertThrows(ResourceNotFoundException.class, () -> bookmarkService.getBookmarkById(invalidBookmarkId));
    }

    @Test
    void ShouldReturnAllBookmarks() { // ShouldReturnAllBookmarks
        //given
        Folder socialMedia = new Folder("Social Media");
        Folder shopping = new Folder("Shopping");
        folderService.saveFolder(null, socialMedia);
        folderService.saveFolder(null, shopping);

        //when
        bookmarkService.saveBookmark(null, socialMedia.getId(), new Bookmark("Facebook", "https://www.facebook.com", socialMedia));
        bookmarkService.saveBookmark(null, socialMedia.getId(), new Bookmark("Twitter", "https://www.twitter.com", socialMedia));
        bookmarkService.saveBookmark(null, shopping.getId(), new Bookmark("Amazon", "https://www.amazon.com", shopping));
        bookmarkService.saveBookmark(null, shopping.getId(), new Bookmark("Ebay", "https://www.ebay.com", shopping));

        //then
        Assertions.assertEquals(4, bookmarkService.getAllBookmarks().size());
    }


    @Test
    public void ShouldUpdateBookmarks() { // ShouldMoveFolders
        // Given
        Folder folder = new Folder("Test Folder");
        folder = folderService.saveFolder(null, folder);
        Long folderId = folder.getId();

        Bookmark bookmark = new Bookmark("Test Title", "Test URL", folder);
        bookmark = bookmarkService.saveBookmark(null, folderId, bookmark);
        Long bookmarkId = bookmark.getId();

        Folder newFolder = new Folder("New Test Folder");
        newFolder = folderService.saveFolder(null, newFolder);
        Long newFolderId = newFolder.getId();

        Bookmark updatedBookmarkDetails = new Bookmark("Updated Test Title", "Updated Test URL");

        // When
        Bookmark updatedBookmark = bookmarkService.updateBookmark(bookmarkId, newFolderId, updatedBookmarkDetails);

        // Then
        Assertions.assertEquals(updatedBookmarkDetails.getUrl(), updatedBookmark.getUrl());
        Assertions.assertEquals(updatedBookmarkDetails.getTitle(), updatedBookmark.getTitle());
        Assertions.assertEquals(newFolder.getId(), updatedBookmark.getFolder().getId());
    }

    @Test
    public void ShouldDeleteBookmarkSuccessfully() {
        //given
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", null);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(null, null, bookmark);
        bookmarkService.deleteBookmark(savedBookmark.getId());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bookmarkService.getBookmarkById(savedBookmark.getId()));
    }

}
