package com.project.assignment.retailer.dto;

import java.util.Map;

public class CustomerDto {

    private String customerId;
    private Map<String, Integer> pointsPerMonth;
    private int totalPoints;

    // Default constructor
    public CustomerDto() {
    }

    // Parameterized constructor
    public CustomerDto(String customerId, Map<String, Integer> pointsPerMonth, int totalPoints) {
        this.customerId = customerId;
        this.pointsPerMonth = pointsPerMonth;
        this.totalPoints = totalPoints;
    }

    // Getters and Setters
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Map<String, Integer> getPointsPerMonth() {
        return pointsPerMonth;
    }

    public void setPointsPerMonth(Map<String, Integer> pointsPerMonth) {
        this.pointsPerMonth = pointsPerMonth;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

}
