package com.java.product.service.exception.custom;

import com.java.product.service.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ResourceConflictException extends BaseException {
    public ResourceConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
