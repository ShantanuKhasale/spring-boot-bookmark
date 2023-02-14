package com.task.bookmark.repository;

import com.task.bookmark.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FolderRepository extends JpaRepository<Folder, Long> {

}
