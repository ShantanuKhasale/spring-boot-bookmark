package com.task.bookmark.repository;

import com.task.bookmark.model.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByFolderId(Long folderId);

    List<Bookmark> findByUserId(Long userId);
}
