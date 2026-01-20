package com.itis403.app.exception;

public class SubmissionNotFoundException extends RuntimeException {

    public SubmissionNotFoundException(String message) {
        super(message);
    }

    public SubmissionNotFoundException(Long submissionId) {
        super("Submission not found with id: " + submissionId);
    }
}