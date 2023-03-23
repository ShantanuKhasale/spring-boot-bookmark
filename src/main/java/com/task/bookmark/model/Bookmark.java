package com.task.bookmark.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bookmarks")
public class Bookmark implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String url;
    @ManyToOne
    @JoinColumn(name = "folder_id")
    @JsonBackReference
    @ToString.Exclude
    private Folder folder;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @ToString.Exclude
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
