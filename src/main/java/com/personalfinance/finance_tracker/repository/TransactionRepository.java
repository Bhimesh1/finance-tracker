package com.personalfinance.finance_tracker.repository;

import com.personalfinance.finance_tracker.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
    Optional<Transaction> findByIdAndUserId(Long id, Long userId);

    Page<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId, Pageable pageable);

    List<Transaction> findByUserIdAndTransactionDateBetween(
            Long userId, LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByUserIdAndAccountId(Long userId, Long accountId);

    List<Transaction> findByUserIdAndCategoryId(Long userId, Long categoryId);

    @Query("SELECT t FROM Transaction t WHERE t.user.id = ?1 " +
            "AND t.transactionDate BETWEEN ?2 AND ?3 " +
            "AND (?4 IS NULL OR t.type = ?4) " +
            "AND (?5 IS NULL OR t.account.id = ?5) " +
            "AND (?6 IS NULL OR t.category.id = ?6) " +
            "ORDER BY t.transactionDate DESC")
    List<Transaction> findByAdvancedFilter(
            Long userId,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Transaction.TransactionType type,
            Long accountId,
            Long categoryId);
}