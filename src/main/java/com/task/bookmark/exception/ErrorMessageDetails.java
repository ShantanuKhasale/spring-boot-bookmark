package com.task.bookmark.exception;

import java.util.Date;

public class ErrorMessageDetails {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorMessageDetails(Date timestamp, String message, String details) {
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

    public String getDetails() {
        return details;
    }
}
