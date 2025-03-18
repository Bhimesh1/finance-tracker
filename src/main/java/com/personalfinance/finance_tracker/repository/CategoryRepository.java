package com.personalfinance.finance_tracker.repository;

import com.personalfinance.finance_tracker.model.Category;
import com.personalfinance.finance_tracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByUser(User user);
    List<Category> findByUserId(Long userId);
    Optional<Category> findByIdAndUserId(Long id, Long userId);
    List<Category> findByUserIdAndType(Long userId, Category.CategoryType type);
}