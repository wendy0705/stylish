package com.example.myproject.exception;

public class InvalidProviderException extends RuntimeException {
    public InvalidProviderException(String message) {
        super(message);
    }
}
