package com.task.bookmark.service;


import com.task.bookmark.AbstractContainerBaseTest;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.BookmarkRepository;
import com.task.bookmark.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.mockito.BDDMockito.given;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BookmarkServiceTest extends AbstractContainerBaseTest {


    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    FolderService folderService;

    @MockBean
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;


    private User user = new User();

    @BeforeAll
    public void beforeAll() {
        userRepository.save(user);
    }


    @BeforeEach
    public void beforeEach() {
        bookmarkRepository.deleteAll();
        given(userService.getCurrentUser()).willReturn(user);
    }

    @Test
    public void shouldCreateBookmarkSuccessfully() {
        // given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", savedFolder);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        // then
        Assertions.assertEquals(bookmark, savedBookmark);
    }

    @Test
    public void shouldGetBookmarkDetailsById() {
        // given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", savedFolder);
        // when
        bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        // then
        Bookmark result = bookmarkService.getBookmarkById(bookmark.getId());
        Assertions.assertEquals(bookmark.getId(), result.getId());
        Assertions.assertEquals(bookmark.getTitle(), result.getTitle());
        Assertions.assertEquals(bookmark.getUrl(), result.getUrl());
    }


    @Test
    public void shouldThrowExceptionForInvalidFolderId() {
        // given
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com");
        // when
        Long invalidFolderId = Long.MAX_VALUE;
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bookmarkService.saveBookmark(invalidFolderId, bookmark));
    }


    @Test
    public void shouldThrowExceptionForInvalidBookmarkId() {
        //given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", savedFolder);
        Long invalidBookmarkId = Long.MAX_VALUE;
        //when
        bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        //then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bookmarkService.getBookmarkById(invalidBookmarkId));
    }


    @Test
    public void shouldReturnAllBookmarks() {
        //given
        Folder socialMedia = new Folder("Social Media");
        Folder shopping = new Folder("Shopping");
        folderService.saveFolder(socialMedia);
        folderService.saveFolder(shopping);
        //when
        bookmarkService.saveBookmark(socialMedia.getId(), new Bookmark("Twitter", "https://www.twitter.com", socialMedia));
        bookmarkService.saveBookmark(socialMedia.getId(), new Bookmark("Facebook", "https://www.facebook.com", socialMedia));
        bookmarkService.saveBookmark(shopping.getId(), new Bookmark("Amazon", "https://www.amazon.com", shopping));
        bookmarkService.saveBookmark(shopping.getId(), new Bookmark("Ebay", "https://www.ebay.com", shopping));
        //then
        Assertions.assertEquals(4, bookmarkService.getAllBookmarks().size());
    }


    @Test
    public void shouldUpdateBookmarks() {
        // Given
        Folder folder = new Folder("Test Folder");
        folder = folderService.saveFolder(folder);
        Long folderId = folder.getId();

        Bookmark bookmark = new Bookmark("Test Title", "Test URL", folder);
        bookmark = bookmarkService.saveBookmark(folderId, bookmark);
        Long bookmarkId = bookmark.getId();

        Folder newFolder = new Folder("New Test Folder");
        newFolder = folderService.saveFolder(newFolder);
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
    public void shouldDeleteBookmarkSuccessfully() {
        //given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", savedFolder);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        bookmarkService.deleteBookmark(savedBookmark.getId());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> bookmarkService.getBookmarkById(savedBookmark.getId()));
    }

    @Test
    public void shouldCreateBookmarksCache() {
        // given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", folder);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        bookmarkService.getAllBookmarks();
        // then
        Assertions.assertTrue(redisTemplate.hasKey("bookmarks::" + user.getId()));
    }

    @Test
    public void shouldClearBookmarksCache() {
        // given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", folder);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        bookmarkService.deleteBookmark(savedBookmark.getId());
        // then
        Assertions.assertFalse(redisTemplate.hasKey("bookmarks::" + user.getId()));
    }


    @Test
    public void shouldCreateBookmarkIdCache() {
        // given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", folder);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        // then
        Assertions.assertTrue(redisTemplate.hasKey("bookmark::" + savedBookmark.getId()));
    }

    @Test
    public void shouldClearBookmarkIdCache() {
        // given
        Folder folder = new Folder("folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Bookmark bookmark = new Bookmark("Google", "https://www.google.com", folder);
        // when
        Bookmark savedBookmark = bookmarkService.saveBookmark(savedFolder.getId(), bookmark);
        bookmarkService.deleteBookmark(savedBookmark.getId());
        // then
        Assertions.assertFalse(redisTemplate.hasKey("bookmark::" + savedBookmark.getId()));
    }

    @Test
    public void shouldThrowOptimisticLockException() throws InterruptedException {
        // given
        Folder folder = new Folder("Folder 1");
        Bookmark bookmark1 = new Bookmark("TITLE1", "URL1");
        Bookmark bookmark2 = new Bookmark("TITLE2", "URL2");
        // when
        Runnable task1 = () -> {
            try {
                bookmarkService.saveBookmark(folder.getId(), bookmark1);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Task 1 Failed");
                Assertions.assertNotNull(e);
            }
        };

        Runnable task2 = () -> {
            try {
                bookmarkService.saveBookmark(folder.getId(), bookmark2);
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Task 2 Failed");
                Assertions.assertNotNull(e);
            }
        };


        Thread t1 = new Thread(() -> {
            folderService.saveFolder(folder); // version = 0
        });

        t1.start();
        t1.join(); // wait for the folder to be saved in database

        Thread t2 = new Thread(task1);
        Thread t3 = new Thread(task2);

        t2.start();
        t3.start();

        t2.join();
        t3.join();

        // then
        Assertions.assertEquals(1, folderService.getFolderById(folder.getId()).getTotalBookmarks());
    }


}
