package com.task.bookmark.service;

import com.task.bookmark.dto.FolderDTO;
import com.task.bookmark.model.Folder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FolderMapper {


    public FolderDTO toFolderDTO(Folder folder) {
        return new FolderDTO(folder);
    }

    public List<FolderDTO> toFolderDTOList(List<Folder> folderList) {
        return folderList.stream().map(folder -> new FolderDTO(folder)).collect(Collectors.toList());
    }

    public Folder toFolder(FolderDTO folderDto) {
        return new Folder(folderDto.getFolderName());
    }

}
