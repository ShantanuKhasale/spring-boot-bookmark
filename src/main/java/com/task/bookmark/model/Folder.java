package com.task.bookmark.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name="folders")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "folderName")
    private String folderName;

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public Folder(){}

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
