package com.task.bookmark.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "folders")
public class Folder implements Serializable {

    private static final long serialVersionUID = 2L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long totalBookmarks = 0L; // Bookmarks counter
    @Version
    private Integer version;
    private String folderName;
    @ToString.Exclude
    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Bookmark> bookmarks;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    @ToString.Exclude
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
