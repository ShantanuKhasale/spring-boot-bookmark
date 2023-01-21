package com.task.bookmark.repository;

import com.task.bookmark.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FolderRepository extends JpaRepository<Folder,Long> {

    @Query(value = "SELECT * FROM folders f WHERE f.folder_name = :folderName", nativeQuery = true)
    public Optional<Folder> findByFolderName(@Param("folderName") String folderName);
}
