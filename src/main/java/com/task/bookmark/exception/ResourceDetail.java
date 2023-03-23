package com.task.bookmark.exception;

import lombok.Data;

@Data
public class ResourceDetail {

    private Long index;
    private String message;

    public ResourceDetail(Long index, String message) {
        this.index = index;
        this.message = message;
    }

    public Long getIndex() {
        return index;
    }

    public String getMessage() {
        return message;
    }

}
