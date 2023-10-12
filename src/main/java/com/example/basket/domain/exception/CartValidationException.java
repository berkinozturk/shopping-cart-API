package com.example.basket.domain.exception;

public class CartValidationException extends RuntimeException {

    public CartValidationException(String message) {
        super(message);
    }
}
