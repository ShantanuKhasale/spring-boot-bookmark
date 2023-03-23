package com.task.bookmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MultipleResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private List<ResourceDetail> resourceDetails;

    public MultipleResourceNotFoundException(String message, List<ResourceDetail> resourceDetails) {
        super(message);
        this.resourceDetails = resourceDetails;
    }

    public List<ResourceDetail> getResourceDetails() {
        return resourceDetails;
    }
}
