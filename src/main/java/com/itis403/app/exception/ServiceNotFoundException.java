package com.itis403.app.exception;

public class ServiceNotFoundException extends RuntimeException {

    public ServiceNotFoundException(String message) {
        super(message);
    }

    public ServiceNotFoundException(Long serviceId) {
        super("Service not found with id: " + serviceId);
    }
}