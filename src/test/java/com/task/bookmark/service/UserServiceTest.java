package com.task.bookmark.service;

import com.task.bookmark.AbstractContainerBaseTest;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.exception.UniqueConstraintException;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertThrows;

@SpringBootTest
//@Transactional
public class UserServiceTest extends AbstractContainerBaseTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;


    @BeforeEach
    public void beforeEach() {
        userRepository.deleteAll();
    }

    @Test
    public void shouldCreateUserSuccessfully() {
        //given
        User user = new User("Test Username", "Test Password");
        //when
        User savedUser = userService.saveUser(user);
        //then
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void shouldGetUserDetailsById() {
        // given
        User user = new User("Shantanu", "123456");
        User savedUser = userService.saveUser(user);
        Long userId = savedUser.getId();
        // when
        User retriveUser = userService.getUserById(userId);
        Assertions.assertEquals(savedUser.getId(), retriveUser.getId());
        Assertions.assertEquals(savedUser.getUsername(), retriveUser.getUsername());
        Assertions.assertEquals(savedUser.getPassword(), retriveUser.getPassword());
    }

    @Test
    public void shouldThrowExceptionForInvalidUserId() {
        //given
        Long userId = Long.MAX_VALUE; // Invalid Id
        // then
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    public void shouldThrowExceptionForDuplicateUsername() {
        //given
        User user1 = new User("USERNAME", "user1");
        userService.saveUser(user1);
        User user2 = new User("USERNAME", "user2");

        // then
        assertThrows(UniqueConstraintException.class, () -> userService.saveUser(user2));
    }

    @Test
    public void shouldReturnAllUsers() {
        //given
        User user1 = new User("user1", "user1");
        User user2 = new User("user2", "user2");
        User user3 = new User("user3", "user3");

        //when
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        //then
        Assertions.assertEquals(3, userService.getAllUsers().size());
    }

    @Test
    public void shouldDeleteUserSuccessfully() {
        // given
        User testUser = new User("Test User", "testUser");
        User savedUser = userService.saveUser(testUser);
        // when
        userService.deleteUser(savedUser.getId());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(savedUser.getId()));
    }

    @Test
    public void shouldUpdateUserDetails() {
        // given
        User user = new User("testUser", "testUser");
        User savedUser = userService.saveUser(user);
        Long userId = savedUser.getId();

        User newUserDetails = new User("newTestUser", "newTestUser");
        // when
        User updatedUser = userService.updateUser(userId, newUserDetails);
        // then
        Assertions.assertEquals(newUserDetails.getUsername(), updatedUser.getUsername());
    }


}
