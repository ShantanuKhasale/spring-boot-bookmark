package com.task.bookmark.exception;

import java.util.Date;
import java.util.List;

public class ValidationErrorDetails {
    private Date timestamp;
    private String message;
    private List<FieldError> details;

    public ValidationErrorDetails(Date timestamp, String message, List<FieldError> details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public List<FieldError> getDetails() {
        return details;
    }
}
