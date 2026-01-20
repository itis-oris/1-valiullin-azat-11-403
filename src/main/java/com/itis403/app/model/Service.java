package com.itis403.app.model;

import java.math.BigDecimal;

public class Service {
    private Long id;
    private Long labelId;
    private String name;
    private String description;
    private BigDecimal basePrice;

    // For JOIN queries
    private String labelName;

    public Service() {}

    public Service(Long labelId, String name, String description, BigDecimal basePrice) {
        this.labelId = labelId;
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getLabelId() { return labelId; }
    public void setLabelId(Long labelId) { this.labelId = labelId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getBasePrice() { return basePrice; }
    public void setBasePrice(BigDecimal basePrice) { this.basePrice = basePrice; }

    public String getLabelName() { return labelName; }
    public void setLabelName(String labelName) { this.labelName = labelName; }
}