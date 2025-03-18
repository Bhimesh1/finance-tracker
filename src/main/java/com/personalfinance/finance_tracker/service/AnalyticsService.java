package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.dto.response.AccountBalanceHistoryResponse;
import com.personalfinance.finance_tracker.dto.response.CashFlowResponse;
import com.personalfinance.finance_tracker.dto.response.ExpenseByCategoryResponse;
import com.personalfinance.finance_tracker.dto.response.MonthlySummaryResponse;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface AnalyticsService {

    List<ExpenseByCategoryResponse> getExpensesByCategory(Long userId, LocalDate startDate, LocalDate endDate);

    Map<String, Object> getDashboardSummary(Long userId);

    CashFlowResponse getCashFlowForMonth(Long userId, YearMonth month);

    MonthlySummaryResponse getMonthlySummary(Long userId, YearMonth startMonth, YearMonth endMonth);

    AccountBalanceHistoryResponse getAccountBalanceHistory(Long userId, Long accountId, LocalDate startDate, LocalDate endDate);

}