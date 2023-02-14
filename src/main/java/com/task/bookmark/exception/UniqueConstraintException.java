package com.task.bookmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
// There was Exception here instead of RuntimeException
public class UniqueConstraintException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UniqueConstraintException(String message) {
        super(message);
    }
}