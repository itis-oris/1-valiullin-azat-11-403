package com.itis403.app.model;

public class ArtistProfile {
    private Long id;
    private Long userId;
    private String artistName;
    private String description;
    private String genre;

    public ArtistProfile() {}

    public ArtistProfile(Long userId, String artistName, String description, String genre) {
        this.userId = userId;
        this.artistName = artistName;
        this.description = description;
        this.genre = genre;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}