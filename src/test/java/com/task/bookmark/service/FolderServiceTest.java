package com.task.bookmark.service;

import com.task.bookmark.AbstractContainerBaseTest;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.BookmarkRepository;
import com.task.bookmark.repository.FolderRepository;
import com.task.bookmark.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import static org.junit.Assert.assertThrows;
import static org.mockito.BDDMockito.given;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FolderServiceTest extends AbstractContainerBaseTest {

    @Autowired
    FolderService folderService;

    @Autowired
    FolderRepository folderRepository;

    @Autowired
    BookmarkService bookmarkService;

    @Autowired
    BookmarkRepository bookmarkRepository;

    @Autowired
    UserRepository userRepository;

    @MockBean
    UserService userService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private User user = new User();

    @BeforeAll
    public void beforeAll() {
        userRepository.save(user);
    }

    @BeforeEach
    public void beforeEach() {
        folderRepository.deleteAll();
        bookmarkRepository.deleteAll();
        given(userService.getCurrentUser()).willReturn(user);
    }

    @Test
    public void shouldCreateFolderSuccessfully() {
        //given
        Folder folder = new Folder("Test Folder");
        //when
        Folder savedFolder = folderService.saveFolder(folder);
        //then
        Assertions.assertEquals(folder, savedFolder);
    }

    @Test
    public void shouldGetFolderDetailsById() {
        // given
        Folder folder = new Folder("Test Folder");
        Folder savedFolder = folderService.saveFolder(folder);
        Long folderId = savedFolder.getId();
        // when
        Folder retriveFolder = folderService.getFolderById(folderId);
        // then
        Assertions.assertEquals(savedFolder, retriveFolder);
    }

    @Test
    public void shouldThrowExceptionForInvalidFolderId() {
        //given
        Long folderId = Long.MAX_VALUE; // Invalid Id
        // then
        assertThrows(ResourceNotFoundException.class, () -> folderService.getFolderById(folderId));
    }

    @Test
    public void shouldReturnAllFolders() {
        //given
        Folder socialMedia = new Folder("Social Media");
        Folder shopping = new Folder("Shopping");
        Folder news = new Folder("News");
        //when
        folderService.saveFolder(socialMedia);
        folderService.saveFolder(shopping);
        folderService.saveFolder(news);
        //then
        Assertions.assertEquals(3, folderService.getAllFolders().size());
    }

    @Test
    public void shouldDeleteFolderSuccessfully() {
        // given
        Folder socialMedia = new Folder("Social Media");
        Folder savedFolder = folderService.saveFolder(socialMedia);
        // when
        folderService.deleteFolder(savedFolder.getId());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> folderService.getFolderById(savedFolder.getId()));
    }

    @Test
    public void shouldReturnAllBookmarksByFolderId() {
        // given
        Folder entertainment = new Folder("Entertainment");
        Folder savedFolder = folderService.saveFolder(entertainment);
        Long folderId = savedFolder.getId();
        Bookmark bookmark1 = new Bookmark("Netflix", "https://www.netflix.com", entertainment);
        Bookmark bookmark2 = new Bookmark("Amazon Prime", "https://www.amazon.com", entertainment);
        bookmarkService.saveBookmark(folderId, bookmark1);
        bookmarkService.saveBookmark(folderId, bookmark2);
        // then
        Assertions.assertEquals(2L, folderService.getFolderById(folderId).getTotalBookmarks());
    }

    @Test
    public void shouldUpdateFolderDetails() {
        // given
        Folder travelFolder = new Folder("Travel");
        Folder savedTravelFolder = folderService.saveFolder(travelFolder);
        Long folderId = savedTravelFolder.getId();
        Folder newTravelFolder = new Folder("TravelPlans");
        // when
        Folder updatedFolder = folderService.updateFolder(folderId, newTravelFolder);
        // then
        Assertions.assertEquals(newTravelFolder.getFolderName(), updatedFolder.getFolderName());
    }


    @Test
    public void shouldCreateFoldersCache() {
        //given
        Folder folder = new Folder("Test Folder");
        //when
        folderService.saveFolder(folder);
        folderService.getAllFolders();
        //then
        Assertions.assertTrue(redisTemplate.hasKey("folders::" + user.getId()));
    }

    @Test
    public void shouldClearFoldersCache() {
        //given
        Folder folder = new Folder("Test Folder");
        //when
        Folder savedFolder = folderService.saveFolder(folder);
        folderService.deleteFolder(savedFolder.getId());
        //then
        Assertions.assertFalse(redisTemplate.hasKey("folders::" + user.getId()));
    }

    @Test
    public void shouldCreateFolderIdCache() {
        //given
        Folder folder = new Folder("Test Folder");
        //when
        Folder savedFolder = folderService.saveFolder(folder);
        //then
        Assertions.assertTrue(redisTemplate.hasKey("folder::" + savedFolder.getId()));
    }

    @Test
    public void shouldClearFolderIdCache() {
        //given
        Folder folder = new Folder("Test Folder");
        //when
        Folder savedFolder = folderService.saveFolder(folder);
        folderService.deleteFolder(savedFolder.getId());
        //then
        Assertions.assertFalse(redisTemplate.hasKey("folder::" + savedFolder.getId()));
    }


    @Test
    public void shouldCreateBookmarksByFolderIdCache() {
        // given
        Folder entertainment = folderService.saveFolder(new Folder("Entertainment"));
        Long folderId = entertainment.getId();
        Bookmark bookmark1 = new Bookmark("Netflix", "https://www.netflix.com", entertainment);
        Bookmark bookmark2 = new Bookmark("Amazon Prime", "https://www.amazon.com", entertainment);
        bookmarkService.saveBookmark(folderId, bookmark1);
        bookmarkService.saveBookmark(folderId, bookmark2);
        // when
        folderService.getBookmarksByFolderId(folderId);
        // then
        Assertions.assertTrue(redisTemplate.hasKey("folder_bookmarks::" + folderId));
    }

    @Test
    public void shouldClearBookmarksByFolderIdCache() {
        // given
        Folder entertainment = folderService.saveFolder(new Folder("Entertainment"));
        Long folderId = entertainment.getId();
        Bookmark bookmark1 = new Bookmark("Netflix", "https://www.netflix.com", entertainment);
        Bookmark bookmark2 = new Bookmark("Amazon Prime", "https://www.amazon.com", entertainment);
        bookmarkService.saveBookmark(folderId, bookmark1);
        bookmarkService.saveBookmark(folderId, bookmark2);
        // when
        folderService.getBookmarksByFolderId(folderId);
        folderService.deleteFolder(folderId);
        // then
        Assertions.assertFalse(redisTemplate.hasKey("folder_bookmarks::" + folderId));
    }


    @Test
    @Transactional
    public void shouldThrowOptimisticLockException() throws InterruptedException {
        // given
        Folder folder = new Folder();
        folder.setFolderName("Folder 1");

        // when
        Runnable task1 = () -> {
            try {
                folderService.updateFolder(folder.getId(), new Folder("Thread 1"));
            } catch (ObjectOptimisticLockingFailureException e) {  // CannotAcquireLockException
                System.out.println("Task 1 Failed");
                Assertions.assertNotNull(e);
            }
        };

        Runnable task2 = () -> {
            try {
                folderService.updateFolder(folder.getId(), new Folder("Thread 2"));
            } catch (ObjectOptimisticLockingFailureException e) {
                System.out.println("Task 2 Failed");
                Assertions.assertNotNull(e);
            }
        };


        Thread t1 = new Thread(() -> {
            folderRepository.save(folder); // version = 0
        });

        t1.start();
        t1.join(); // wait for folder to be saved in database

        Thread t2 = new Thread(task1);
        Thread t3 = new Thread(task2);

        t2.start();
        t3.start();

        t2.join();
        t3.join();

        // then
        Assertions.assertNotEquals("Folder 1", folderService.getFolderById(folder.getId()).getFolderName());
    }

}
