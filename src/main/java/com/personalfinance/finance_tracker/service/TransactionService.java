package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.dto.request.TransactionRequest;
import com.personalfinance.finance_tracker.dto.response.TransactionResponse;
import com.personalfinance.finance_tracker.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {

    Page<TransactionResponse> getTransactionsByUser(Long userId, Pageable pageable);

    List<TransactionResponse> getTransactionsByDateRange(Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<TransactionResponse> getTransactionsByAccount(Long userId, Long accountId);

    List<TransactionResponse> getTransactionsByCategory(Long userId, Long categoryId);

    List<TransactionResponse> getTransactionsByAdvancedFilter(
            Long userId, LocalDateTime startDate, LocalDateTime endDate,
            Transaction.TransactionType type, Long accountId, Long categoryId);

    TransactionResponse getTransactionById(Long id, Long userId);

    TransactionResponse createTransaction(TransactionRequest transactionRequest, Long userId);

    TransactionResponse updateTransaction(Long id, TransactionRequest transactionRequest, Long userId);

    void deleteTransaction(Long id, Long userId);

}