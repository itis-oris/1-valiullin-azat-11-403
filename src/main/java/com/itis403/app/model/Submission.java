package com.itis403.app.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Submission {
    private Long id;
    private Long artistId;
    private Long serviceId;
    private String trackTitle;
    private String trackFileUrl;
    private SubmissionStatus status;
    private LocalDateTime submissionDate;
    private String labelComment;
    private LocalDateTime administeredDate;

    // For JOIN queries
    private String artistName;
    private String serviceName;
    private String labelName;

    public Submission() {}

    public Submission(Long artistId, Long serviceId, String trackTitle, String trackFileUrl) {
        this.artistId = artistId;
        this.serviceId = serviceId;
        this.trackTitle = trackTitle;
        this.trackFileUrl = trackFileUrl;
        this.status = SubmissionStatus.PENDING;
        this.submissionDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getArtistId() { return artistId; }
    public void setArtistId(Long artistId) { this.artistId = artistId; }

    public Long getServiceId() { return serviceId; }
    public void setServiceId(Long serviceId) { this.serviceId = serviceId; }

    public String getTrackTitle() { return trackTitle; }
    public void setTrackTitle(String trackTitle) { this.trackTitle = trackTitle; }

    public String getTrackFileUrl() { return trackFileUrl; }
    public void setTrackFileUrl(String trackFileUrl) { this.trackFileUrl = trackFileUrl; }

    public SubmissionStatus getStatus() { return status; }
    public void setStatus(SubmissionStatus status) { this.status = status; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }

    public String getLabelComment() { return labelComment; }
    public void setLabelComment(String labelComment) { this.labelComment = labelComment; }

    public LocalDateTime getAdministeredDate() { return administeredDate; }
    public void setAdministeredDate(LocalDateTime administeredDate) { this.administeredDate = administeredDate; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getLabelName() { return labelName; }
    public void setLabelName(String labelName) { this.labelName = labelName; }

    public String getFormattedSubmissionDate() {
        if (submissionDate == null) return "Unknown date";
        return submissionDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
    }

    public String getShortSubmissionDate() {
        if (submissionDate == null) return "Unknown date";
        return submissionDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));
    }

    public String getIsoSubmissionDate() {
        if (submissionDate == null) return "";
        return submissionDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
    public String getFormattedAdministeredDate() {
        if (administeredDate == null) return "Not reviewed";
        return administeredDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
    }
}