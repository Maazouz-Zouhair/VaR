package com.gms.var.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;


public class VaRRequest {
    @NotNull(message = "File must not be null")
    private MultipartFile file;

    @NotNull(message = "Confidence level must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Confidence level must be between 0.0 and 1.0")
    @DecimalMax(value = "1.0", inclusive = false, message = "Confidence level must be between 0.0 and 1.0")
    private Double confidenceLevel;

    // Getters and Setters
    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public Double getConfidenceLevel() {
        return confidenceLevel;
    }

    public void setConfidenceLevel(Double confidenceLevel) {
        this.confidenceLevel = confidenceLevel;
    }
}
