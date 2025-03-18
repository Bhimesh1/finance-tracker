package com.personalfinance.finance_tracker.dto.response;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public class CashFlowResponse {

    private YearMonth period;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;
    private BigDecimal netCashFlow;
    private List<CashFlowItemResponse> incomeItems;
    private List<CashFlowItemResponse> expenseItems;

    // Nested class for cash flow items
    public static class CashFlowItemResponse {
        private Long categoryId;
        private String categoryName;
        private String categoryColor;
        private BigDecimal amount;

        // Default constructor
        public CashFlowItemResponse() {
        }

        // Constructor with fields
        public CashFlowItemResponse(Long categoryId, String categoryName, String categoryColor, BigDecimal amount) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.categoryColor = categoryColor;
            this.amount = amount;
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
    }

    // Default constructor
    public CashFlowResponse() {
    }

    // Constructor with fields
    public CashFlowResponse(YearMonth period, BigDecimal totalIncome, BigDecimal totalExpense,
                            List<CashFlowItemResponse> incomeItems, List<CashFlowItemResponse> expenseItems) {
        this.period = period;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.netCashFlow = totalIncome.subtract(totalExpense);
        this.incomeItems = incomeItems;
        this.expenseItems = expenseItems;
    }

    // Getters and setters
    public YearMonth getPeriod() {
        return period;
    }

    public void setPeriod(YearMonth period) {
        this.period = period;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(BigDecimal totalIncome) {
        this.totalIncome = totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(BigDecimal totalExpense) {
        this.totalExpense = totalExpense;
    }

    public BigDecimal getNetCashFlow() {
        return netCashFlow;
    }

    public void setNetCashFlow(BigDecimal netCashFlow) {
        this.netCashFlow = netCashFlow;
    }

    public List<CashFlowItemResponse> getIncomeItems() {
        return incomeItems;
    }

    public void setIncomeItems(List<CashFlowItemResponse> incomeItems) {
        this.incomeItems = incomeItems;
    }

    public List<CashFlowItemResponse> getExpenseItems() {
        return expenseItems;
    }

    public void setExpenseItems(List<CashFlowItemResponse> expenseItems) {
        this.expenseItems = expenseItems;
    }
}