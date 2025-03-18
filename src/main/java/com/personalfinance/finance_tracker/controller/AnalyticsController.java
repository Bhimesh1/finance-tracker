package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.dto.response.AccountBalanceHistoryResponse;
import com.personalfinance.finance_tracker.dto.response.CashFlowResponse;
import com.personalfinance.finance_tracker.dto.response.ExpenseByCategoryResponse;
import com.personalfinance.finance_tracker.dto.response.MonthlySummaryResponse;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/expenses-by-category")
    public ResponseEntity<List<ExpenseByCategoryResponse>> getExpensesByCategory(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<ExpenseByCategoryResponse> expenses = analyticsService.getExpensesByCategory(
                userDetails.getId(), startDate, endDate);

        return ResponseEntity.ok(expenses);
    }

    @GetMapping("/dashboard-summary")
    public ResponseEntity<Map<String, Object>> getDashboardSummary(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Map<String, Object> summary = analyticsService.getDashboardSummary(userDetails.getId());

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/cash-flow/{year}/{month}")
    public ResponseEntity<CashFlowResponse> getCashFlowForMonth(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        YearMonth yearMonth = YearMonth.of(year, month);
        CashFlowResponse cashFlow = analyticsService.getCashFlowForMonth(userDetails.getId(), yearMonth);

        return ResponseEntity.ok(cashFlow);
    }

    @GetMapping("/monthly-summary")
    public ResponseEntity<MonthlySummaryResponse> getMonthlySummary(
            @RequestParam int startYear,
            @RequestParam int startMonth,
            @RequestParam int endYear,
            @RequestParam int endMonth,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        YearMonth startYearMonth = YearMonth.of(startYear, startMonth);
        YearMonth endYearMonth = YearMonth.of(endYear, endMonth);

        MonthlySummaryResponse summary = analyticsService.getMonthlySummary(
                userDetails.getId(), startYearMonth, endYearMonth);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/account-balance-history/{accountId}")
    public ResponseEntity<AccountBalanceHistoryResponse> getAccountBalanceHistory(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        AccountBalanceHistoryResponse balanceHistory = analyticsService.getAccountBalanceHistory(
                userDetails.getId(), accountId, startDate, endDate);

        return ResponseEntity.ok(balanceHistory);
    }
}