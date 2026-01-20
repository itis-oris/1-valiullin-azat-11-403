package com.itis403.app.util;

public class Constants {

    // File upload constants
    public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
    public static final String UPLOAD_DIR = "uploads";
    public static final String[] ALLOWED_AUDIO_TYPES = {
            "audio/mpeg", "audio/wav", "audio/mp3"
    };

    // Session constants
    public static final int SESSION_TIMEOUT_MINUTES = 30;

    // Pagination
    public static final int DEFAULT_PAGE_SIZE = 10;
    public static final int MAX_PAGE_SIZE = 50;

    // Business rules
    public static final int MAX_SUBMISSIONS_PER_ARTIST = 20;
    public static final int MAX_SERVICES_PER_LABEL = 10;

    // File paths
    public static final String AUDIO_UPLOAD_PATH = "audio";
    public static final String IMAGE_UPLOAD_PATH = "images";
}