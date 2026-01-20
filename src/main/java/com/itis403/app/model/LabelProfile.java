package com.itis403.app.model;

public class LabelProfile {
    private Long id;
    private Long userId;
    private String labelName;
    private String description;
    private String contactEmail;
    private String website;

    public LabelProfile() {}

    // Исправленный конструктор
    public LabelProfile(Long userId, String labelName, String description, String contactEmail, String website) {
        this.userId = userId;
        this.labelName = labelName;
        this.description = description;
        this.contactEmail = contactEmail;
        this.website = website;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getLabelName() { return labelName; }
    public void setLabelName(String labelName) { this.labelName = labelName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getWebsite() { return website; }
    public void setWebsite(String website) { this.website = website; }
}