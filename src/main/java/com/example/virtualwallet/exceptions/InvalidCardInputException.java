package com.example.virtualwallet.exceptions;

public class InvalidCardInputException extends RuntimeException {
    public InvalidCardInputException(String message) {
        super(message);
    }
}
