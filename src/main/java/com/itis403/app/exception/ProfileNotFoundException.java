package com.itis403.app.exception;

public class ProfileNotFoundException extends RuntimeException {

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(String profileType, Long userId) {
        super(profileType + " profile not found for user id: " + userId);
    }
}