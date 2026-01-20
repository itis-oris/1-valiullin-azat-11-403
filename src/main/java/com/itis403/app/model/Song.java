package com.itis403.app.model;

import java.time.LocalDateTime;

public class Song {
    private Long id;
    private Long artistId;
    private String title;
    private String genre;
    private String fileUrl;
    private Integer duration; // in seconds
    private Long fileSize; // in bytes
    private LocalDateTime uploadedAt;
    private Boolean isApproved;
    private LocalDateTime approvedAt;

    // For JOIN queries
    private String artistName;

    public Song() {}

    public Song(Long artistId, String title, String genre, String fileUrl, Integer duration, Long fileSize) {
        this.artistId = artistId;
        this.title = title;
        this.genre = genre;
        this.fileUrl = fileUrl;
        this.duration = duration;
        this.fileSize = fileSize;
        this.uploadedAt = LocalDateTime.now();
        this.isApproved = false;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getArtistId() { return artistId; }
    public void setArtistId(Long artistId) { this.artistId = artistId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean isApproved) { this.isApproved = isApproved; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }
}