package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.dto.request.FinancialGoalRequest;
import com.personalfinance.finance_tracker.dto.response.FinancialGoalResponse;
import com.personalfinance.finance_tracker.model.FinancialGoal;

import java.util.List;

public interface FinancialGoalService {

    List<FinancialGoalResponse> getAllGoalsByUser(Long userId);

    List<FinancialGoalResponse> getGoalsByStatus(Long userId, FinancialGoal.GoalStatus status);

    FinancialGoalResponse getGoalById(Long id, Long userId);

    FinancialGoalResponse createGoal(FinancialGoalRequest goalRequest, Long userId);

    FinancialGoalResponse updateGoal(Long id, FinancialGoalRequest goalRequest, Long userId);

    FinancialGoalResponse updateGoalProgress(Long id, Long userId, java.math.BigDecimal newAmount);

    void deleteGoal(Long id, Long userId);

}