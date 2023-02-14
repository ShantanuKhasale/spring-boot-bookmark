package com.task.bookmark.service;

import com.task.bookmark.AbstractContainerBaseTest;
import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.exception.UniqueConstraintException;
import com.task.bookmark.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertThrows;

@SpringBootTest
@Transactional
public class UserServiceTest extends AbstractContainerBaseTest {

    @Autowired
    UserService userService;


    @Test
    public void ShouldCreateUserSuccessfully() {
        //given
        User user = new User("Test Username", "Test Password");
        //when
        User savedUser = userService.saveUser(user);
        //then
        Assertions.assertEquals(user, savedUser);
    }

    @Test
    public void ShouldGetUserDetailsById() {
        // given
        User user = new User("Shantanu", "123456");
        User savedUser = userService.saveUser(user);
        Long userId = savedUser.getId();
        // when
        User retriveUser = userService.getUserById(userId);
        Assertions.assertEquals(savedUser, retriveUser);
    }

    @Test
    public void ShouldThrowExceptionForInvalidUserId() {
        //given
        Long userId = 56L; // Invalid Id
        // then
        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(userId));
    }

    @Test
    public void ShouldThrowExceptionForDuplicateUsername() {
        //given
        User user1 = new User("USERNAME", "user1");
        userService.saveUser(user1);
        User user2 = new User("USERNAME", "user2");

        // then
        assertThrows(UniqueConstraintException.class, () -> userService.saveUser(user2));
    }

    @Test
    public void ShouldReturnAllUsers() {
        //given
        User user1 = new User("user1", "user1");
        User user2 = new User("user2", "user2");
        User user3 = new User("user3", "user3");

        //when
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);

        //then
        Assertions.assertTrue(userService.getAllUsers().containsAll(List.of(user1, user2, user3)));
    }

    @Test
    public void ShouldDeleteUserSuccessfully() {
        // given
        User testUser = new User("Test User", "testUser");
        User savedUser = userService.saveUser(testUser);
        // when
        userService.deleteUser(savedUser.getId());
        // then
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(savedUser.getId()));
    }

    @Test
    public void ShouldUpdateUserDetails() {
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
