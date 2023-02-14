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

import java.util.List;

import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
public class FolderServiceTest extends AbstractContainerBaseTest {

    @Autowired
    FolderService folderService;

    @Autowired
    BookmarkService bookmarkService;

    @Test
    public void ShouldCreateFolderSuccessfully() {
        //given
        Folder folder = new Folder("Test Folder");
        //when
        Folder savedFolder = folderService.saveFolder(null, folder);
        //then
        Assertions.assertEquals(folder, savedFolder);
    }

    @Test
    public void ShouldGetFolderDetailsById() {
        // given
        Folder folder = new Folder("Test Folder");
        Folder savedFolder = folderService.saveFolder(null, folder);
        Long folderId = savedFolder.getId();
        // when
        Folder retriveFolder = folderService.getFolderById(folderId);
        // then
        Assertions.assertEquals(savedFolder, retriveFolder);
    }

    @Test
    public void ShouldThrowExceptionForInvalidFolderId() {
        //given
        Long folderId = 56L; // Invalid Id
        // then
        assertThrows(ResourceNotFoundException.class, () -> folderService.getFolderById(folderId));
    }

    @Test
    public void ShouldReturnAllFolders() {
        //given
        Folder socialMedia = new Folder("Social Media");
        Folder shopping = new Folder("Shopping");
        Folder news = new Folder("News");
        //when
        folderService.saveFolder(null, socialMedia);
        folderService.saveFolder(null, shopping);
        folderService.saveFolder(null, news);
        //then
        Assertions.assertTrue(folderService.getAllFolders().containsAll(List.of(socialMedia, shopping, news)));
    }

    @Test
    public void ShouldDeleteFolderSuccessfully() {
        // given
        Folder socialMedia = new Folder("Social Media");
        Folder savedFolder = folderService.saveFolder(null, socialMedia);
        // when
        folderService.deleteFolder(savedFolder.getId());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> folderService.getFolderById(savedFolder.getId()));
    }

    @Test
    public void ShouldReturnAllBookmarksByFolderId() {
        // given
        Folder entertainment = new Folder("Entertainment");
        Folder savedFolder = folderService.saveFolder(null, entertainment);
        Long folderId = savedFolder.getId();
        Bookmark bookmark1 = new Bookmark("Netflix", "https://www.netflix.com", entertainment);
        Bookmark bookmark2 = new Bookmark("Amazon Prime", "https://www.amazon.com", entertainment);
        bookmarkService.saveBookmark(null, folderId, bookmark1);
        bookmarkService.saveBookmark(null, folderId, bookmark2);
        // then
        Assertions.assertTrue(folderService.getBookmarksByFolderId(folderId).containsAll(List.of(bookmark1, bookmark2)));
    }

    @Test
    public void ShouldUpdateFolderDetails() {
        // given
        Folder travelFolder = new Folder("Travel");
        Folder savedTravelFolder = folderService.saveFolder(null, travelFolder);
        Long folderId = savedTravelFolder.getId();
        Folder newTravelFolder = new Folder("TravelPlans");
        // when
        Folder updatedFolder = folderService.updateFolder(folderId, newTravelFolder);
        // then
        Assertions.assertEquals(newTravelFolder.getFolderName(), updatedFolder.getFolderName());
    }

}
