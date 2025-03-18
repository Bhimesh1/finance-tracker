package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.dto.response.NotificationResponse;
import com.personalfinance.finance_tracker.exception.ResourceNotFoundException;
import com.personalfinance.finance_tracker.model.*;
import com.personalfinance.finance_tracker.repository.NotificationRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import com.personalfinance.finance_tracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<NotificationResponse> getAllNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationResponse> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false).stream()
                .map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long id, Long userId) {
        Notification notification = notificationRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));

        notification.setRead(true);

        Notification updatedNotification = notificationRepository.save(notification);

        return new NotificationResponse(updatedNotification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadOrderByCreatedAtDesc(userId, false);

        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
    }

    @Override
    public int getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndRead(userId, false);
    }

    @Override
    @Transactional
    public void createBudgetAlert(Budget budget, BigDecimal spentAmount, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        BigDecimal budgetAmount = budget.getAmount();
        BigDecimal spentPercentage = spentAmount.multiply(new BigDecimal("100"))
                .divide(budgetAmount, 2, RoundingMode.HALF_UP);

        // Create alert if spending has reached 80% or exceeded the budget
        if (spentPercentage.compareTo(new BigDecimal("80")) >= 0) {
            String categoryName = budget.getCategory().getName();
            String monthYear = budget.getPeriod().format(DateTimeFormatter.ofPattern("MMMM yyyy"));
            String message;

            if (spentPercentage.compareTo(new BigDecimal("100")) >= 0) {
                message = "Budget exceeded for " + categoryName + " in " + monthYear + ". " +
                        "You've spent " + spentPercentage + "% of your budget.";
            } else {
                message = "You've used " + spentPercentage + "% of your " + categoryName + " budget for " + monthYear + ".";
            }

            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.BUDGET_ALERT);
            notification.setUser(user);

            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void createGoalDeadlineAlert(FinancialGoal goal, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        LocalDate today = LocalDate.now();
        LocalDate targetDate = goal.getTargetDate();

        // Create alert if the goal deadline is approaching (within 7 days) or has passed
        if (targetDate.isAfter(today) && targetDate.isBefore(today.plusDays(8))) {
            long daysLeft = today.until(targetDate).getDays();
            String dayText = daysLeft == 1 ? "day" : "days";

            double progressPercentage = goal.getProgressPercentage();
            String message = "Your goal '" + goal.getName() + "' is due in " + daysLeft + " " + dayText + ". " +
                    "Current progress: " + progressPercentage + "%.";

            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.GOAL_DEADLINE);
            notification.setUser(user);

            notificationRepository.save(notification);
        }
        // The deadline has passed and the goal is not achieved
        else if (targetDate.isBefore(today) && goal.getStatus() != FinancialGoal.GoalStatus.ACHIEVED) {
            String message = "The deadline for your goal '" + goal.getName() + "' has passed. " +
                    "You've reached " + goal.getProgressPercentage() + "% of your target.";

            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.GOAL_DEADLINE);
            notification.setUser(user);

            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void createTransactionAlert(Transaction transaction, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        // Only create alerts for large transactions (for example, over $1000)
        BigDecimal threshold = new BigDecimal("1000");
        if (transaction.getAmount().compareTo(threshold) > 0) {
            String message = "Large " + transaction.getType().toString().toLowerCase() + " of $" +
                    transaction.getAmount() + " recorded in " + transaction.getAccount().getName() + ".";

            Notification notification = new Notification();
            notification.setMessage(message);
            notification.setType(Notification.NotificationType.ACCOUNT_UPDATE);
            notification.setUser(user);

            notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public void createSystemNotification(String message, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setType(Notification.NotificationType.SYSTEM);
        notification.setUser(user);

        notificationRepository.save(notification);
    }
}