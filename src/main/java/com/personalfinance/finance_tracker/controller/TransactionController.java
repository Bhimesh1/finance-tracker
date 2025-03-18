package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.dto.request.TransactionRequest;
import com.personalfinance.finance_tracker.dto.response.TransactionResponse;
import com.personalfinance.finance_tracker.model.Transaction;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getAllTransactions(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 20, sort = "transactionDate") Pageable pageable) {
        Page<TransactionResponse> transactions = transactionService.getTransactionsByUser(userDetails.getId(), pageable);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByDateRange(
                userDetails.getId(), startDate, endDate);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(
            @PathVariable Long accountId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByAccount(
                userDetails.getId(), accountId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByCategory(
            @PathVariable Long categoryId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByCategory(
                userDetails.getId(), categoryId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAdvancedFilter(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) Long accountId,
            @RequestParam(required = false) Long categoryId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<TransactionResponse> transactions = transactionService.getTransactionsByAdvancedFilter(
                userDetails.getId(), startDate, endDate, type, accountId, categoryId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TransactionResponse transaction = transactionService.getTransactionById(id, userDetails.getId());
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest transactionRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TransactionResponse createdTransaction = transactionService.createTransaction(
                transactionRequest, userDetails.getId());
        return ResponseEntity.ok(createdTransaction);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @PathVariable Long id,
            @Valid @RequestBody TransactionRequest transactionRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        TransactionResponse updatedTransaction = transactionService.updateTransaction(
                id, transactionRequest, userDetails.getId());
        return ResponseEntity.ok(updatedTransaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        transactionService.deleteTransaction(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}