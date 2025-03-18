package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.dto.request.BudgetRequest;
import com.personalfinance.finance_tracker.dto.response.BudgetResponse;

import java.time.YearMonth;
import java.util.List;

public interface BudgetService {

    List<BudgetResponse> getAllBudgetsByUser(Long userId);

    List<BudgetResponse> getBudgetsByPeriod(Long userId, YearMonth period);

    BudgetResponse getBudgetById(Long id, Long userId);

    BudgetResponse createBudget(BudgetRequest budgetRequest, Long userId);

    BudgetResponse updateBudget(Long id, BudgetRequest budgetRequest, Long userId);

    void deleteBudget(Long id, Long userId);

}