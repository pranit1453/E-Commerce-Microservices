package com.java.inventory.service.exception.custom;

import com.java.inventory.service.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ResourceConflictException extends BaseException {
    public ResourceConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
