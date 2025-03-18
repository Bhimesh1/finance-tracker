package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.dto.request.BudgetRequest;
import com.personalfinance.finance_tracker.dto.response.BudgetResponse;
import com.personalfinance.finance_tracker.exception.ResourceNotFoundException;
import com.personalfinance.finance_tracker.model.Budget;
import com.personalfinance.finance_tracker.model.Category;
import com.personalfinance.finance_tracker.model.Transaction;
import com.personalfinance.finance_tracker.model.User;
import com.personalfinance.finance_tracker.repository.BudgetRepository;
import com.personalfinance.finance_tracker.repository.CategoryRepository;
import com.personalfinance.finance_tracker.repository.TransactionRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import com.personalfinance.finance_tracker.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public List<BudgetResponse> getAllBudgetsByUser(Long userId) {
        return budgetRepository.findByUserId(userId).stream()
                .map(budget -> new BudgetResponse(budget, calculateSpentAmount(userId, budget.getCategory().getId(), budget.getPeriod())))
                .collect(Collectors.toList());
    }

    @Override
    public List<BudgetResponse> getBudgetsByPeriod(Long userId, YearMonth period) {
        return budgetRepository.findByUserIdAndPeriod(userId, period).stream()
                .map(budget -> new BudgetResponse(budget, calculateSpentAmount(userId, budget.getCategory().getId(), period)))
                .collect(Collectors.toList());
    }

    @Override
    public BudgetResponse getBudgetById(Long id, Long userId) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));

        BigDecimal spentAmount = calculateSpentAmount(userId, budget.getCategory().getId(), budget.getPeriod());

        return new BudgetResponse(budget, spentAmount);
    }

    @Override
    @Transactional
    public BudgetResponse createBudget(BudgetRequest budgetRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = categoryRepository.findByIdAndUserId(budgetRequest.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + budgetRequest.getCategoryId()));

        // Check if budget already exists for this category and period
        Optional<Budget> existingBudget = budgetRepository.findByUserIdAndCategoryIdAndPeriod(
                userId, category.getId(), budgetRequest.getPeriod());

        if (existingBudget.isPresent()) {
            throw new IllegalStateException("Budget already exists for this category and period");
        }

        Budget budget = new Budget();
        budget.setCategory(category);
        budget.setAmount(budgetRequest.getAmount());
        budget.setPeriod(budgetRequest.getPeriod());
        budget.setUser(user);

        Budget savedBudget = budgetRepository.save(budget);

        BigDecimal spentAmount = calculateSpentAmount(userId, category.getId(), budgetRequest.getPeriod());

        return new BudgetResponse(savedBudget, spentAmount);
    }

    @Override
    @Transactional
    public BudgetResponse updateBudget(Long id, BudgetRequest budgetRequest, Long userId) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));

        Category category = categoryRepository.findByIdAndUserId(budgetRequest.getCategoryId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + budgetRequest.getCategoryId()));

        // If category or period changed, check for conflicts
        if (!budget.getCategory().getId().equals(category.getId()) ||
                !budget.getPeriod().equals(budgetRequest.getPeriod())) {

            Optional<Budget> existingBudget = budgetRepository.findByUserIdAndCategoryIdAndPeriod(
                    userId, category.getId(), budgetRequest.getPeriod());

            if (existingBudget.isPresent() && !existingBudget.get().getId().equals(id)) {
                throw new IllegalStateException("Budget already exists for this category and period");
            }
        }

        budget.setCategory(category);
        budget.setAmount(budgetRequest.getAmount());
        budget.setPeriod(budgetRequest.getPeriod());

        Budget updatedBudget = budgetRepository.save(budget);

        BigDecimal spentAmount = calculateSpentAmount(userId, category.getId(), budgetRequest.getPeriod());

        return new BudgetResponse(updatedBudget, spentAmount);
    }

    @Override
    @Transactional
    public void deleteBudget(Long id, Long userId) {
        Budget budget = budgetRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found with id: " + id));

        budgetRepository.delete(budget);
    }

/**
 * Calculates the amount spent in a specific category during a period.
 * @param userId The user ID
 * @param categoryId The category ID
 * @param period The year and month
 * @return The total amount spent
 */
private BigDecimal calculateSpentAmount(Long userId, Long categoryId, YearMonth period) {
    // Get start and end of the month
    LocalDateTime startOfMonth = period.atDay(1).atStartOfDay();
    LocalDateTime endOfMonth = period.atEndOfMonth().atTime(23, 59, 59);

    // Find all transactions for this user, category, and time period
    List<Transaction> transactions = transactionRepository.findByUserIdAndCategoryId(userId, categoryId).stream()
            .filter(t -> !t.getTransactionDate().isBefore(startOfMonth) && !t.getTransactionDate().isAfter(endOfMonth))
            .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
            .collect(Collectors.toList());

    // Sum up transaction amounts
    return transactions.stream()
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
}
}