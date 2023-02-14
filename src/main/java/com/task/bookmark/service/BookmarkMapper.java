package com.task.bookmark.service;

import com.task.bookmark.dto.BookmarkDTO;
import com.task.bookmark.model.Bookmark;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookmarkMapper {

    public BookmarkDTO toBookmarkDTO(Bookmark bookmark) {
        return new BookmarkDTO(bookmark);
    }

    public List<BookmarkDTO> toBookmarkDTOList(List<Bookmark> bookmarkList) {
        return bookmarkList.stream().map(bookmark -> new BookmarkDTO(bookmark)).collect(Collectors.toList());
    }

    public Bookmark toBookmark(BookmarkDTO bookmarkDTO) {
        return new Bookmark(bookmarkDTO.getTitle(), bookmarkDTO.getUrl());
    }

}
