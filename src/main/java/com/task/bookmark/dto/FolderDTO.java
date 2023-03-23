package com.task.bookmark.dto;

import com.task.bookmark.model.Folder;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FolderDTO {

    private Long id;
    @NotBlank(message = "Folder Name Cannot Be Empty.")
    private String folderName;
//    @NotNull(message = "Enter Valid userId")
//    private Long userId;

    private Long totalBookmarks;

    public FolderDTO(Folder folder) {
        this.id = folder.getId();
        this.folderName = folder.getFolderName();
        this.totalBookmarks = folder.getTotalBookmarks();
//        this.userId = folder.hasUser() ? folder.getUser().getId() : null;
    }
}
