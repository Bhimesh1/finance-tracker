package com.personalfinance.finance_tracker.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class AccountBalanceHistoryResponse {

    private Long accountId;
    private String accountName;
    private List<BalancePoint> balanceHistory;

    public static class BalancePoint {
        private LocalDate date;
        private BigDecimal balance;

        // Default constructor
        public BalancePoint() {
        }

        // Constructor with fields
        public BalancePoint(LocalDate date, BigDecimal balance) {
            this.date = date;
            this.balance = balance;
        }

        // Getters and setters
        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }

        public BigDecimal getBalance() {
            return balance;
        }

        public void setBalance(BigDecimal balance) {
            this.balance = balance;
        }
    }

    // Default constructor
    public AccountBalanceHistoryResponse() {
    }

    // Constructor with fields
    public AccountBalanceHistoryResponse(Long accountId, String accountName, List<BalancePoint> balanceHistory) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.balanceHistory = balanceHistory;
    }

    // Getters and setters
    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<BalancePoint> getBalanceHistory() {
        return balanceHistory;
    }

    public void setBalanceHistory(List<BalancePoint> balanceHistory) {
        this.balanceHistory = balanceHistory;
    }
}