package com.task.bookmark;


import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.BookmarkRepository;
import com.task.bookmark.repository.FolderRepository;
import com.task.bookmark.repository.UserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;


@SpringBootApplication
public class BookmarkApplication {

    public static void main(String[] args) {
//		SpringApplication.run(BookmarkApplication.class, args);

        ApplicationContext context = SpringApplication.run(BookmarkApplication.class, args);
        FolderRepository folderRepository = context.getBean(FolderRepository.class);
        BookmarkRepository bookmarkRepository = context.getBean(BookmarkRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User demoUser = new User("shantanu", bCryptPasswordEncoder.encode("123456"));

        userRepository.save(demoUser);

        Folder socialMedia = new Folder("Social Media", demoUser);
        Folder shopping = new Folder("Shopping", demoUser);
        Folder entertainment = new Folder("Entertainment", demoUser);
        Folder news = new Folder("News", demoUser);
        Folder travel = new Folder("Travel", demoUser);

        folderRepository.saveAll(Arrays.asList(socialMedia, shopping, entertainment, news, travel));

        bookmarkRepository.saveAll(Arrays.asList(
                new Bookmark("Facebook", "https://www.facebook.com", socialMedia, demoUser),
                new Bookmark("Twitter", "https://www.twitter.com", socialMedia, demoUser),
                new Bookmark("Amazon", "https://www.amazon.com", shopping, demoUser),
                new Bookmark("Ebay", "https://www.ebay.com", shopping, demoUser),
                new Bookmark("Netflix", "https://www.netflix.com", entertainment, demoUser),
                new Bookmark("CNN", "https://www.cnn.com", news, demoUser),
                new Bookmark("BBC", "https://www.bbc.com", news, demoUser),
                new Bookmark("TripAdvisor", "https://www.tripadvisor.com", travel, demoUser)
        ));
    }

}
