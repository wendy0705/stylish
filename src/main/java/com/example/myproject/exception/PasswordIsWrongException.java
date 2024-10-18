package com.example.myproject.exception;

public class PasswordIsWrongException extends RuntimeException {
    public PasswordIsWrongException(String message) {
        super(message);
    }
}
