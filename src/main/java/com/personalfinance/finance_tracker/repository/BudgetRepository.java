package com.personalfinance.finance_tracker.repository;

import com.personalfinance.finance_tracker.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {

    List<Budget> findByUserIdAndPeriod(Long userId, YearMonth period);

    Optional<Budget> findByUserIdAndCategoryIdAndPeriod(Long userId, Long categoryId, YearMonth period);

    List<Budget> findByUserId(Long userId);

    Optional<Budget> findByIdAndUserId(Long id, Long userId);

}