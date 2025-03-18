package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.dto.response.NotificationResponse;
import com.personalfinance.finance_tracker.model.Budget;
import com.personalfinance.finance_tracker.model.FinancialGoal;
import com.personalfinance.finance_tracker.model.Notification;
import com.personalfinance.finance_tracker.model.Transaction;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

public interface NotificationService {

    List<NotificationResponse> getAllNotifications(Long userId);

    List<NotificationResponse> getUnreadNotifications(Long userId);

    NotificationResponse markAsRead(Long id, Long userId);

    void markAllAsRead(Long userId);

    int getUnreadCount(Long userId);

    void createBudgetAlert(Budget budget, BigDecimal spentAmount, Long userId);

    void createGoalDeadlineAlert(FinancialGoal goal, Long userId);

    void createTransactionAlert(Transaction transaction, Long userId);

    void createSystemNotification(String message, Long userId);

}