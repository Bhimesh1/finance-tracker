package com.personalfinance.finance_tracker.dto.response;

import com.personalfinance.finance_tracker.model.Budget;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;

public class BudgetResponse {

    private Long id;
    private CategoryResponse category;
    private BigDecimal amount;
    private YearMonth period;
    private BigDecimal spentAmount;
    private double spentPercentage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public BudgetResponse() {
    }

    // Constructor from Budget entity
    public BudgetResponse(Budget budget, BigDecimal spentAmount) {
        this.id = budget.getId();
        this.category = budget.getCategory() != null ? new CategoryResponse(budget.getCategory()) : null;
        this.amount = budget.getAmount();
        this.period = budget.getPeriod();
        this.spentAmount = spentAmount;
        this.spentPercentage = calculateSpentPercentage(budget.getAmount(), spentAmount);
        this.createdAt = budget.getCreatedAt();
        this.updatedAt = budget.getUpdatedAt();
    }

    // All-args constructor
    public BudgetResponse(Long id, CategoryResponse category, BigDecimal amount, YearMonth period,
                          BigDecimal spentAmount, double spentPercentage,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.period = period;
        this.spentAmount = spentAmount;
        this.spentPercentage = spentPercentage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Calculate spent percentage
    private double calculateSpentPercentage(BigDecimal budgetAmount, BigDecimal spentAmount) {
        if (budgetAmount.compareTo(BigDecimal.ZERO) == 0) {
            return 0;
        }
        return spentAmount.multiply(new BigDecimal("100"))
                .divide(budgetAmount, 2, BigDecimal.ROUND_HALF_UP)
                .doubleValue();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CategoryResponse getCategory() {
        return category;
    }

    public void setCategory(CategoryResponse category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public BigDecimal getSpentAmount() {
        return spentAmount;
    }

    public void setSpentAmount(BigDecimal spentAmount) {
        this.spentAmount = spentAmount;
    }

    public double getSpentPercentage() {
        return spentPercentage;
    }

    public void setSpentPercentage(double spentPercentage) {
        this.spentPercentage = spentPercentage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}