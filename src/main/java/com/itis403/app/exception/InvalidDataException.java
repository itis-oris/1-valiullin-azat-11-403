package com.itis403.app.exception;

public class InvalidDataException extends RuntimeException {

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String field, String value) {
        super("Invalid " + field + ": " + value);
    }
}