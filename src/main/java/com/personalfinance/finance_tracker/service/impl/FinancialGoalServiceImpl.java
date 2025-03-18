package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.dto.request.FinancialGoalRequest;
import com.personalfinance.finance_tracker.dto.response.FinancialGoalResponse;
import com.personalfinance.finance_tracker.exception.ResourceNotFoundException;
import com.personalfinance.finance_tracker.model.Account;
import com.personalfinance.finance_tracker.model.FinancialGoal;
import com.personalfinance.finance_tracker.model.User;
import com.personalfinance.finance_tracker.repository.AccountRepository;
import com.personalfinance.finance_tracker.repository.FinancialGoalRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import com.personalfinance.finance_tracker.service.FinancialGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinancialGoalServiceImpl implements FinancialGoalService {

    @Autowired
    private FinancialGoalRepository goalRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<FinancialGoalResponse> getAllGoalsByUser(Long userId) {
        return goalRepository.findByUserId(userId).stream()
                .map(FinancialGoalResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<FinancialGoalResponse> getGoalsByStatus(Long userId, FinancialGoal.GoalStatus status) {
        return goalRepository.findByUserIdAndStatus(userId, status).stream()
                .map(FinancialGoalResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public FinancialGoalResponse getGoalById(Long id, Long userId) {
        FinancialGoal goal = goalRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial goal not found with id: " + id));

        return new FinancialGoalResponse(goal);
    }

    @Override
    @Transactional
    public FinancialGoalResponse createGoal(FinancialGoalRequest goalRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account = null;
        if (goalRequest.getAccountId() != null) {
            account = accountRepository.findByIdAndUserId(goalRequest.getAccountId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + goalRequest.getAccountId()));
        }

        FinancialGoal goal = new FinancialGoal();
        goal.setName(goalRequest.getName());
        goal.setDescription(goalRequest.getDescription());
        goal.setTargetAmount(goalRequest.getTargetAmount());
        goal.setCurrentAmount(goalRequest.getCurrentAmount() != null ? goalRequest.getCurrentAmount() : BigDecimal.ZERO);
        goal.setTargetDate(goalRequest.getTargetDate());
        goal.setStatus(FinancialGoal.GoalStatus.IN_PROGRESS);
        goal.setAccount(account);
        goal.setUser(user);

        // Update status based on current amount
        updateGoalStatus(goal);

        FinancialGoal savedGoal = goalRepository.save(goal);

        return new FinancialGoalResponse(savedGoal);
    }

    @Override
    @Transactional
    public FinancialGoalResponse updateGoal(Long id, FinancialGoalRequest goalRequest, Long userId) {
        FinancialGoal goal = goalRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial goal not found with id: " + id));

        Account account = null;
        if (goalRequest.getAccountId() != null) {
            account = accountRepository.findByIdAndUserId(goalRequest.getAccountId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + goalRequest.getAccountId()));
        }

        goal.setName(goalRequest.getName());
        goal.setDescription(goalRequest.getDescription());
        goal.setTargetAmount(goalRequest.getTargetAmount());

        if (goalRequest.getCurrentAmount() != null) {
            goal.setCurrentAmount(goalRequest.getCurrentAmount());
        }

        goal.setTargetDate(goalRequest.getTargetDate());
        goal.setAccount(account);

        // Update status based on current amount and target date
        updateGoalStatus(goal);

        FinancialGoal updatedGoal = goalRepository.save(goal);

        return new FinancialGoalResponse(updatedGoal);
    }

    @Override
    @Transactional
    public FinancialGoalResponse updateGoalProgress(Long id, Long userId, BigDecimal newAmount) {
        FinancialGoal goal = goalRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial goal not found with id: " + id));

        goal.setCurrentAmount(newAmount);

        // Update status based on new amount
        updateGoalStatus(goal);

        FinancialGoal updatedGoal = goalRepository.save(goal);

        return new FinancialGoalResponse(updatedGoal);
    }

    @Override
    @Transactional
    public void deleteGoal(Long id, Long userId) {
        FinancialGoal goal = goalRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Financial goal not found with id: " + id));

        goalRepository.delete(goal);
    }

    /**
     * Updates the goal status based on current amount and target date.
     *
     * @param goal The financial goal to update
     */
    private void updateGoalStatus(FinancialGoal goal) {
        // Check if the goal has been achieved
        if (goal.getCurrentAmount().compareTo(goal.getTargetAmount()) >= 0) {
            goal.setStatus(FinancialGoal.GoalStatus.ACHIEVED);
        }
        // Check if the goal has failed (past target date but not achieved)
        else if (goal.getTargetDate().isBefore(LocalDate.now())) {
            goal.setStatus(FinancialGoal.GoalStatus.FAILED);
        }
        // Otherwise, the goal is still in progress
        else {
            goal.setStatus(FinancialGoal.GoalStatus.IN_PROGRESS);
        }
    }
}