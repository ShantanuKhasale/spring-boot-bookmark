package com.task.bookmark.service;

import com.task.bookmark.exception.ResourceNotFoundException;
import com.task.bookmark.exception.UniqueConstraintException;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user) throws UniqueConstraintException {

        userRepository.findByUsername(user.getUsername()).ifPresent(u -> {
            throw new UniqueConstraintException("Username already exists");
        });

        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
        userRepository.delete(user);
    }


    public User updateUser(Long userId, User userDetails) throws ResourceNotFoundException, UniqueConstraintException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found for ID ::" + userId));

        userRepository.findByUsername(userDetails.getUsername()).ifPresent(userFound -> {
            throw new UniqueConstraintException("Username already exists");
        });

        user.setUsername(userDetails.getUsername());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }


    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User Not found for ID ::" + userId));
        return user;
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return Optional.ofNullable(authentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(User.class::cast)
                .orElseThrow(() -> new UsernameNotFoundException("User not logged in"));
    }
}
