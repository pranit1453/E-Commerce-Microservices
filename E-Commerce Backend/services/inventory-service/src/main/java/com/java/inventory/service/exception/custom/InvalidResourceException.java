package com.java.inventory.service.exception.custom;

import com.java.inventory.service.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidResourceException extends BaseException {
    public InvalidResourceException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
