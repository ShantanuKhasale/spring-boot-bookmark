package com.task.bookmark.service;

import com.task.bookmark.dto.BookmarkDTO;
import com.task.bookmark.model.Bookmark;
import com.task.bookmark.model.CreateBookmark;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class BookmarkMapper {

    public BookmarkDTO toBookmarkDTO(Bookmark bookmark) {
        return new BookmarkDTO(bookmark);
    }

    public Bookmark toBookmark(BookmarkDTO bookmarkDTO) {
        return new Bookmark(bookmarkDTO.getTitle(), bookmarkDTO.getUrl());
    }

    public List<BookmarkDTO> toBookmarkDTOList(List<Bookmark> bookmarkList) {
        return bookmarkList.stream().map(bookmark -> new BookmarkDTO(bookmark)).collect(Collectors.toList());
    }

    public List<Bookmark> toBookmarkList(List<BookmarkDTO> bookmarkDTOList) {
        return bookmarkDTOList.stream().map(bookmark -> new Bookmark(bookmark.getTitle(), bookmark.getUrl())).collect(Collectors.toList());
    }

    public CreateBookmark toBookmarkReference(BookmarkDTO bookmark) {
        return new CreateBookmark(bookmark);
    }

    public List<CreateBookmark> toBookmarkReferenceList(List<BookmarkDTO> bookmarkDTOList) {
        return bookmarkDTOList.stream().map(bookmark -> new CreateBookmark(bookmark)).collect(Collectors.toList());
    }


}
