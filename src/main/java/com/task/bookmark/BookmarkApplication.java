package com.task.bookmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class BookmarkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmarkApplication.class, args);
	}

}
