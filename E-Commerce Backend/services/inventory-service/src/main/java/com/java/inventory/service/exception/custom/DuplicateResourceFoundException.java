package com.java.inventory.service.exception.custom;

import com.java.inventory.service.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicateResourceFoundException extends BaseException {
    public DuplicateResourceFoundException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
