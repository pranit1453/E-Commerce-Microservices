package com.java.product.service.exception.custom;

import com.java.product.service.exception.BaseException;
import org.springframework.http.HttpStatus;

public class InsufficientStockException extends BaseException {
    public InsufficientStockException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
