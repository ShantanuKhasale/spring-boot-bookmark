package com.task.bookmark.model;

import jakarta.persistence.*;

@Entity
@Table(name="bookmarks")
public class Bookmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "title")
    private String title;
    @Column(name = "url")
    private String url;

    @Column(name = "folderId")
    private String folderId;

    public Bookmark(){}
    public Bookmark(String title, String url, String folderId) {
        this.title = title;
        this.url = url;
        this.folderId=folderId;
    }

    public String getFolderId() {
        return folderId;
    }

    public void setFolderId(String folderId) {
        this.folderId = folderId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
