package com.example.myproject.exception;

public class JWTException extends RuntimeException {
    public JWTException(String message, Throwable cause) {
        super(message, cause);
    }
}
