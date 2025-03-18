package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.model.Budget;
import com.personalfinance.finance_tracker.model.FinancialGoal;
import com.personalfinance.finance_tracker.model.Transaction;
import com.personalfinance.finance_tracker.repository.BudgetRepository;
import com.personalfinance.finance_tracker.repository.FinancialGoalRepository;
import com.personalfinance.finance_tracker.repository.TransactionRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduledTasksService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private FinancialGoalRepository goalRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Daily check for budget alerts
     * Runs at midnight every day
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkBudgets() {
        YearMonth currentMonth = YearMonth.now();

        List<Budget> budgets = budgetRepository.findAll().stream()
                .filter(budget -> budget.getPeriod().equals(currentMonth))
                .collect(Collectors.toList());

        for (Budget budget : budgets) {
            // Calculate spent amount
            LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(23, 59, 59);

            BigDecimal spentAmount = transactionRepository.findByUserIdAndCategoryId(budget.getUser().getId(), budget.getCategory().getId())
                    .stream()
                    .filter(t -> !t.getTransactionDate().isBefore(startOfMonth) && !t.getTransactionDate().isAfter(endOfMonth))
                    .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                    .map(Transaction::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Create notifications if necessary
            notificationService.createBudgetAlert(budget, spentAmount, budget.getUser().getId());
        }
    }

    /**
     * Daily check for financial goal deadlines
     * Runs at midnight every day
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkGoalDeadlines() {
        LocalDate today = LocalDate.now();

        List<FinancialGoal> goals = goalRepository.findAll().stream()
                .filter(goal -> goal.getStatus() == FinancialGoal.GoalStatus.IN_PROGRESS)
                .filter(goal -> {
                    // Include goals with upcoming deadlines (next 7 days) or passed deadlines
                    LocalDate targetDate = goal.getTargetDate();
                    return targetDate.isBefore(today.plusDays(8)) || targetDate.isBefore(today);
                })
                .collect(Collectors.toList());

        for (FinancialGoal goal : goals) {
            // Create notifications for approaching or passed deadlines
            notificationService.createGoalDeadlineAlert(goal, goal.getUser().getId());

            // Update goal status if deadline has passed
            if (goal.getTargetDate().isBefore(today)) {
                // If the goal is achieved, set status to ACHIEVED
                if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
                    goal.setStatus(FinancialGoal.GoalStatus.ACHIEVED);
                } else {
                    goal.setStatus(FinancialGoal.GoalStatus.FAILED);
                }
                goalRepository.save(goal);
            }
        }
    }
}