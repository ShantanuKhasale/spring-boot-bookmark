package com.task.bookmark.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;
    @ManyToOne
    @JoinColumn(name = "folder_id")
    @JsonManagedReference
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Bookmark(String title, String url, Folder folder) {
        this.title = title;
        this.url = url;
        this.folder = folder;
    }

    public Bookmark(String title, String url, Folder folder, User user) {
        this.title = title;
        this.url = url;
        this.folder = folder;
        this.user = user;
    }

    public Bookmark(String title, String url) {
        this.title = title;
        this.url = url;
        this.folder = null;
    }

    public boolean hasFolder() {
        return this.folder != null;
    }

    public boolean hasUser() {
        return this.user != null;
    }
}
