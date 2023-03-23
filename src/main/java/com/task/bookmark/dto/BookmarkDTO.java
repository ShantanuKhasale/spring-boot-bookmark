package com.task.bookmark.dto;

import com.task.bookmark.model.Bookmark;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkDTO {
    private Long id;

    @NotBlank(message = "Title Must Not Be Empty")
    private String title;
    @NotBlank(message = "URL Must Not Be Empty ")
    @URL(message = "Enter Valid URL")
    private String url;
    @NotNull(message = "Enter Valid folderId")
    private Long folderId;

//    @NotNull(message = "Enter Valid userId")
//    private Long userId;


    public BookmarkDTO(Bookmark bookmark) {
        this.id = bookmark.getId();
        this.title = bookmark.getTitle();
        this.url = bookmark.getUrl();
        this.folderId = bookmark.hasFolder() ? bookmark.getFolder().getId() : null;
//        this.userId = bookmark.hasUser() ? bookmark.getUser().getId() : null;
    }

}
