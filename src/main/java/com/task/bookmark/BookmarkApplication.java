package com.task.bookmark;


import com.task.bookmark.model.CreateBookmark;
import com.task.bookmark.model.Folder;
import com.task.bookmark.model.Role;
import com.task.bookmark.model.User;
import com.task.bookmark.repository.UserRepository;
import com.task.bookmark.service.BookmarkService;
import com.task.bookmark.service.FolderService;
import com.task.bookmark.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;


@SpringBootApplication
@EnableCaching
public class BookmarkApplication {


    public static void main(String[] args) {
//		SpringApplication.run(BookmarkApplication.class, args);

        ApplicationContext context = SpringApplication.run(BookmarkApplication.class, args);
//        FolderRepository folderRepository = context.getBean(FolderRepository.class);
//        BookmarkRepository bookmarkRepository = context.getBean(BookmarkRepository.class);
        UserRepository userRepository = context.getBean(UserRepository.class);

        FolderService folderService = context.getBean(FolderService.class);
        BookmarkService bookmarkService = context.getBean(BookmarkService.class);
        UserService userService = context.getBean(UserService.class);

        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        User demoUser = new User("shantanu", bCryptPasswordEncoder.encode("123456"));
        demoUser.setRole(Role.USER);
//        userRepository.save(demoUser);
        userService.saveUser(demoUser);
        UserDetailsService userDetailsService = username -> userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not found"));
        UserDetails userDetails = userDetailsService.loadUserByUsername(demoUser.getUsername());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);


        Folder socialMedia = new Folder("Social Media", demoUser);
        Folder shopping = new Folder("Shopping", demoUser);
        Folder entertainment = new Folder("Entertainment", demoUser);
        Folder news = new Folder("News", demoUser);
        Folder travel = new Folder("Travel", demoUser);

        folderService.saveFolder(socialMedia);
        folderService.saveFolder(shopping);
        folderService.saveFolder(entertainment);
        folderService.saveFolder(news);
        folderService.saveFolder(travel);

//        folderRepository.saveAll(Arrays.asList(socialMedia, shopping, entertainment, news, travel));

//        bookmarkRepository.saveAll(Arrays.asList(
//                new Bookmark("Facebook", "https://www.facebook.com", socialMedia, demoUser),
//                new Bookmark("Twitter", "https://www.twitter.com", socialMedia, demoUser),
//                new Bookmark("Amazon", "https://www.amazon.com", shopping, demoUser),
//                new Bookmark("Ebay", "https://www.ebay.com", shopping, demoUser),
//                new Bookmark("Netflix", "https://www.netflix.com", entertainment, demoUser),
//                new Bookmark("CNN", "https://www.cnn.com", news, demoUser),
//                new Bookmark("BBC", "https://www.bbc.com", news, demoUser),
//                new Bookmark("TripAdvisor", "https://www.tripadvisor.com", travel, demoUser)
//        ));

        List<CreateBookmark> bookmarks = List.of(
                new CreateBookmark("Facebook", "https://www.facebook.com", socialMedia.getId()),
                new CreateBookmark("Twitter", "https://www.twitter.com", socialMedia.getId()),
                new CreateBookmark("Amazon", "https://www.amazon.com", shopping.getId()),
                new CreateBookmark("Ebay", "https://www.ebay.com", shopping.getId()),
                new CreateBookmark("Netflix", "https://www.netflix.com", entertainment.getId()),
                new CreateBookmark("CNN", "https://www.cnn.com", news.getId()),
                new CreateBookmark("BBC", "https://www.bbc.com", news.getId()),
                new CreateBookmark("TripAdvisor", "https://www.tripadvisor.com", travel.getId())
        );

        bookmarkService.saveBookmarks(bookmarks);
    }

}
