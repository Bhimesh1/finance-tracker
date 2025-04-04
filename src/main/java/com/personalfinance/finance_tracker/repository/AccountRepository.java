package com.personalfinance.finance_tracker.repository;

import com.personalfinance.finance_tracker.model.Account;
import com.personalfinance.finance_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
    List<Account> findByUserId(Long userId);
    Optional<Account> findByIdAndUserId(Long id, Long userId);
}