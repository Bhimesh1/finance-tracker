package com.personalfinance.finance_tracker.dto.response;

import com.personalfinance.finance_tracker.model.FinancialGoal;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class FinancialGoalResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount;
    private LocalDate targetDate;
    private FinancialGoal.GoalStatus status;
    private AccountResponse account;
    private double progressPercentage;
    private int daysRemaining;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public FinancialGoalResponse() {
    }

    // Constructor from FinancialGoal entity
    public FinancialGoalResponse(FinancialGoal goal) {
        this.id = goal.getId();
        this.name = goal.getName();
        this.description = goal.getDescription();
        this.targetAmount = goal.getTargetAmount();
        this.currentAmount = goal.getCurrentAmount();
        this.targetDate = goal.getTargetDate();
        this.status = goal.getStatus();
        this.account = goal.getAccount() != null ? new AccountResponse(goal.getAccount()) : null;
        this.progressPercentage = goal.getProgressPercentage();
        this.daysRemaining = calculateDaysRemaining(goal.getTargetDate());
        this.createdAt = goal.getCreatedAt();
        this.updatedAt = goal.getUpdatedAt();
    }

    // All-args constructor
    public FinancialGoalResponse(Long id, String name, String description, BigDecimal targetAmount,
                                 BigDecimal currentAmount, LocalDate targetDate, FinancialGoal.GoalStatus status,
                                 AccountResponse account, double progressPercentage, int daysRemaining,
                                 LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount;
        this.targetDate = targetDate;
        this.status = status;
        this.account = account;
        this.progressPercentage = progressPercentage;
        this.daysRemaining = daysRemaining;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Calculate days remaining until target date
    private int calculateDaysRemaining(LocalDate targetDate) {
        LocalDate today = LocalDate.now();
        if (targetDate.isBefore(today)) {
            return 0;
        }
        return (int) java.time.temporal.ChronoUnit.DAYS.between(today, targetDate);
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public FinancialGoal.GoalStatus getStatus() {
        return status;
    }

    public void setStatus(FinancialGoal.GoalStatus status) {
        this.status = status;
    }

    public AccountResponse getAccount() {
        return account;
    }

    public void setAccount(AccountResponse account) {
        this.account = account;
    }

    public double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public int getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(int daysRemaining) {
        this.daysRemaining = daysRemaining;
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