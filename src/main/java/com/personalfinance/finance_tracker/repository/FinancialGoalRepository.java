package com.personalfinance.finance_tracker.repository;

import com.personalfinance.finance_tracker.model.FinancialGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialGoalRepository extends JpaRepository<FinancialGoal, Long> {

    List<FinancialGoal> findByUserId(Long userId);

    List<FinancialGoal> findByUserIdAndStatus(Long userId, FinancialGoal.GoalStatus status);

    Optional<FinancialGoal> findByIdAndUserId(Long id, Long userId);

}