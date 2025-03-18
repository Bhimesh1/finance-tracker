package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.dto.request.TransactionRequest;
import com.personalfinance.finance_tracker.dto.response.TransactionResponse;
import com.personalfinance.finance_tracker.exception.ResourceNotFoundException;
import com.personalfinance.finance_tracker.model.Account;
import com.personalfinance.finance_tracker.model.Budget;
import com.personalfinance.finance_tracker.model.Category;
import com.personalfinance.finance_tracker.model.Transaction;
import com.personalfinance.finance_tracker.model.User;
import com.personalfinance.finance_tracker.repository.AccountRepository;
import com.personalfinance.finance_tracker.repository.BudgetRepository;
import com.personalfinance.finance_tracker.repository.CategoryRepository;
import com.personalfinance.finance_tracker.repository.TransactionRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import com.personalfinance.finance_tracker.service.NotificationService;
import com.personalfinance.finance_tracker.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private NotificationService notificationService;

    @Override
    public Page<TransactionResponse> getTransactionsByUser(Long userId, Pageable pageable) {
        return transactionRepository.findByUserIdOrderByTransactionDateDesc(userId, pageable)
                .map(TransactionResponse::new);
    }

    @Override
    public List<TransactionResponse> getTransactionsByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return transactionRepository.findByUserIdAndTransactionDateBetween(userId, startDate, endDate).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByAccount(Long userId, Long accountId) {
        return transactionRepository.findByUserIdAndAccountId(userId, accountId).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByCategory(Long userId, Long categoryId) {
        return transactionRepository.findByUserIdAndCategoryId(userId, categoryId).stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionResponse> getTransactionsByAdvancedFilter(
            Long userId, LocalDateTime startDate, LocalDateTime endDate,
            Transaction.TransactionType type, Long accountId, Long categoryId) {
        return transactionRepository.findByAdvancedFilter(userId, startDate, endDate, type, accountId, categoryId)
                .stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getTransactionById(Long id, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        return new TransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse createTransaction(TransactionRequest transactionRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account = accountRepository.findByIdAndUserId(transactionRequest.getAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + transactionRequest.getAccountId()));

        Category category = null;
        if (transactionRequest.getCategoryId() != null) {
            category = categoryRepository.findByIdAndUserId(transactionRequest.getCategoryId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + transactionRequest.getCategoryId()));
        }

        Transaction transaction = new Transaction();
        transaction.setDescription(transactionRequest.getDescription());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setType(transactionRequest.getType());
        transaction.setTransactionDate(transactionRequest.getTransactionDate());
        transaction.setAccount(account);
        transaction.setCategory(category);
        transaction.setNotes(transactionRequest.getNotes());
        transaction.setUser(user);

        // Update account balance
        updateAccountBalance(account, transaction, true);

        Transaction savedTransaction = transactionRepository.save(transaction);

        // Create transaction notification for large amounts
        notificationService.createTransactionAlert(savedTransaction, userId);

        // Check if this transaction affects any budget
        if (category != null && transaction.getType() == Transaction.TransactionType.EXPENSE) {
            YearMonth transactionMonth = YearMonth.from(transaction.getTransactionDate());
            Optional<Budget> budget = budgetRepository.findByUserIdAndCategoryIdAndPeriod(userId, category.getId(), transactionMonth);

            if (budget.isPresent()) {
                // Get all transactions for this category and month
                LocalDateTime startOfMonth = transactionMonth.atDay(1).atStartOfDay();
                LocalDateTime endOfMonth = transactionMonth.atEndOfMonth().atTime(23, 59, 59);

                BigDecimal totalSpent = transactionRepository.findByUserIdAndCategoryId(userId, category.getId()).stream()
                        .filter(t -> !t.getTransactionDate().isBefore(startOfMonth) && !t.getTransactionDate().isAfter(endOfMonth))
                        .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                        .map(Transaction::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                // Check if we should create a budget alert
                notificationService.createBudgetAlert(budget.get(), totalSpent, userId);
            }
        }

        return new TransactionResponse(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse updateTransaction(Long id, TransactionRequest transactionRequest, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        // Revert the previous transaction's effect on the account balance
        updateAccountBalance(transaction.getAccount(), transaction, false);

        Account newAccount = accountRepository.findByIdAndUserId(transactionRequest.getAccountId(), userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + transactionRequest.getAccountId()));

        Category newCategory = null;
        if (transactionRequest.getCategoryId() != null) {
            newCategory = categoryRepository.findByIdAndUserId(transactionRequest.getCategoryId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + transactionRequest.getCategoryId()));
        }

        transaction.setDescription(transactionRequest.getDescription());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setType(transactionRequest.getType());
        transaction.setTransactionDate(transactionRequest.getTransactionDate());
        transaction.setAccount(newAccount);
        transaction.setCategory(newCategory);
        transaction.setNotes(transactionRequest.getNotes());

        // Apply the updated transaction's effect on the account balance
        updateAccountBalance(newAccount, transaction, true);

        Transaction updatedTransaction = transactionRepository.save(transaction);

        return new TransactionResponse(updatedTransaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));

        // Revert the transaction's effect on the account balance
        updateAccountBalance(transaction.getAccount(), transaction, false);

        transactionRepository.delete(transaction);
    }

    /**
     * Updates the account balance based on the transaction.
     *
     * @param account The account to update
     * @param transaction The transaction affecting the balance
     * @param isAddition True if adding the transaction effect, false if removing
     */
    private void updateAccountBalance(Account account, Transaction transaction, boolean isAddition) {
        BigDecimal currentBalance = account.getBalance();
        BigDecimal transactionAmount = transaction.getAmount();

        BigDecimal newBalance;
        if (isAddition) {
            if (transaction.getType() == Transaction.TransactionType.INCOME) {
                newBalance = currentBalance.add(transactionAmount);
            } else {
                newBalance = currentBalance.subtract(transactionAmount);
            }
        } else {
            if (transaction.getType() == Transaction.TransactionType.INCOME) {
                newBalance = currentBalance.subtract(transactionAmount);
            } else {
                newBalance = currentBalance.add(transactionAmount);
            }
        }

        account.setBalance(newBalance);
        accountRepository.save(account);
    }
}