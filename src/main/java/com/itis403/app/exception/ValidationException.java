package com.itis403.app.exception;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String requirement) {
        super("Field '" + field + "' " + requirement);
    }
}