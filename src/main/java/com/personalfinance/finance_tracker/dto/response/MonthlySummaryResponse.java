package com.personalfinance.finance_tracker.dto.response;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class MonthlySummaryResponse {

    private List<MonthlyDataPoint> data;

    public static class MonthlyDataPoint {
        private YearMonth month;
        private BigDecimal income;
        private BigDecimal expense;
        private BigDecimal savings;
        private double savingsRate; // Savings as percentage of income

        // Default constructor
        public MonthlyDataPoint() {
        }

        // Constructor with fields
        public MonthlyDataPoint(YearMonth month, BigDecimal income, BigDecimal expense) {
            this.month = month;
            this.income = income;
            this.expense = expense;
            this.savings = income.subtract(expense);

            // Calculate savings rate
            if (income.compareTo(BigDecimal.ZERO) > 0) {
                this.savingsRate = this.savings.multiply(new BigDecimal("100"))
                        .divide(income, 2, BigDecimal.ROUND_HALF_UP)
                        .doubleValue();
            } else {
                this.savingsRate = 0;
            }
        }

        // Getters and setters
        public YearMonth getMonth() {
            return month;
        }

        public void setMonth(YearMonth month) {
            this.month = month;
        }

        public BigDecimal getIncome() {
            return income;
        }

        public void setIncome(BigDecimal income) {
            this.income = income;
        }

        public BigDecimal getExpense() {
            return expense;
        }

        public void setExpense(BigDecimal expense) {
            this.expense = expense;
        }

        public BigDecimal getSavings() {
            return savings;
        }

        public void setSavings(BigDecimal savings) {
            this.savings = savings;
        }

        public double getSavingsRate() {
            return savingsRate;
        }

        public void setSavingsRate(double savingsRate) {
            this.savingsRate = savingsRate;
        }
    }

    // Default constructor
    public MonthlySummaryResponse() {
    }

    // Constructor with fields
    public MonthlySummaryResponse(List<MonthlyDataPoint> data) {
        this.data = data;
    }

    // Getters and setters
    public List<MonthlyDataPoint> getData() {
        return data;
    }

    public void setData(List<MonthlyDataPoint> data) {
        this.data = data;
    }
}