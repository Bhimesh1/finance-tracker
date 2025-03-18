package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.dto.request.CategoryRequest;
import com.personalfinance.finance_tracker.dto.response.CategoryResponse;
import com.personalfinance.finance_tracker.model.Category;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategoriesByUser(Long userId);

    List<CategoryResponse> getCategoriesByType(Long userId, Category.CategoryType type);

    CategoryResponse getCategoryById(Long id, Long userId);

    CategoryResponse createCategory(CategoryRequest categoryRequest, Long userId);

    CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest, Long userId);

    void deleteCategory(Long id, Long userId);

}