package com.itis403.app.exception;

public class DatabaseException extends RuntimeException {

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatabaseException(String operation, String entity, Throwable cause) {
        super("Failed to " + operation + " " + entity, cause);
    }
}