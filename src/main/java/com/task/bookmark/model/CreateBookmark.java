package com.task.bookmark.model;

import com.task.bookmark.dto.BookmarkDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBookmark {
    private String title;
    private String url;
    private Long folderId;


    public CreateBookmark(BookmarkDTO bookmark) {
        this.title = bookmark.getTitle();
        this.url = bookmark.getUrl();
        this.folderId = bookmark.getFolderId();
    }
}
