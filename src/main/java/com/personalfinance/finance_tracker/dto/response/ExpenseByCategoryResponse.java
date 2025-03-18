package com.personalfinance.finance_tracker.dto.response;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExpenseByCategoryResponse {

    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private BigDecimal amount;
    private double percentage;

    // Default constructor
    public ExpenseByCategoryResponse() {
    }

    // Constructor with fields
    public ExpenseByCategoryResponse(Long categoryId, String categoryName, String categoryColor,
                                     BigDecimal amount, BigDecimal totalAmount) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
        this.amount = amount;
        this.percentage = calculatePercentage(amount, totalAmount);
    }

    // Calculate percentage
    private double calculatePercentage(BigDecimal amount, BigDecimal totalAmount) {
        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return amount.multiply(new BigDecimal("100"))
                .divide(totalAmount, 2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    // Getters and setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryColor() {
        return categoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        this.categoryColor = categoryColor;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}