package com.task.bookmark.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "folders")
public class Folder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String folderName;
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Bookmark> bookmarks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    public Folder(String folderName) {
        this.folderName = folderName;
    }

    public Folder(String folderName, User user) {
        this.folderName = folderName;
        this.user = user;
    }

    public boolean hasUser() {
        return this.user != null;
    }

}
