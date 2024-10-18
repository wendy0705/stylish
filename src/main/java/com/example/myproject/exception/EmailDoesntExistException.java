package com.example.myproject.exception;

public class EmailDoesntExistException extends RuntimeException {
    public EmailDoesntExistException(String message) {
        super(message);
    }
}
