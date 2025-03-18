package com.personalfinance.finance_tracker.dto.response;

import com.personalfinance.finance_tracker.model.Account;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccountResponse {

    private Long id;
    private String name;
    private String description;
    private Account.AccountType type;
    private BigDecimal balance;
    private String institution;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public AccountResponse() {
    }

    // Constructor from Account entity
    public AccountResponse(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.description = account.getDescription();
        this.type = account.getType();
        this.balance = account.getBalance();
        this.institution = account.getInstitution();
        this.createdAt = account.getCreatedAt();
        this.updatedAt = account.getUpdatedAt();
    }

    // All-args constructor
    public AccountResponse(Long id, String name, String description, Account.AccountType type,
                           BigDecimal balance, String institution,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
        this.balance = balance;
        this.institution = institution;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Account.AccountType getType() {
        return type;
    }

    public void setType(Account.AccountType type) {
        this.type = type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
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