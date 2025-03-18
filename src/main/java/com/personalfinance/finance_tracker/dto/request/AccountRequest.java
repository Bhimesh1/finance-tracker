package com.personalfinance.finance_tracker.dto.request;

import com.personalfinance.finance_tracker.model.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class AccountRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Account.AccountType type;

    @NotNull
    @Positive
    private BigDecimal balance;

    private String institution;

    // Default constructor
    public AccountRequest() {
    }

    // All-args constructor
    public AccountRequest(String name, String description, Account.AccountType type,
                          BigDecimal balance, String institution) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.balance = balance;
        this.institution = institution;
    }

    // Getters and setters
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
}