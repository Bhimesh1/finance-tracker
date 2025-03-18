package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.dto.request.CategoryRequest;
import com.personalfinance.finance_tracker.dto.response.CategoryResponse;
import com.personalfinance.finance_tracker.exception.ResourceNotFoundException;
import com.personalfinance.finance_tracker.model.Category;
import com.personalfinance.finance_tracker.model.User;
import com.personalfinance.finance_tracker.repository.CategoryRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import com.personalfinance.finance_tracker.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<CategoryResponse> getAllCategoriesByUser(Long userId) {
        return categoryRepository.findByUserId(userId).stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> getCategoriesByType(Long userId, Category.CategoryType type) {
        return categoryRepository.findByUserIdAndType(userId, type).stream()
                .map(CategoryResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long id, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        return new CategoryResponse(category);
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest categoryRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setType(categoryRequest.getType());
        category.setColor(categoryRequest.getColor());
        category.setIcon(categoryRequest.getIcon());
        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest categoryRequest, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setType(categoryRequest.getType());
        category.setColor(categoryRequest.getColor());
        category.setIcon(categoryRequest.getIcon());

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponse(updatedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id, Long userId) {
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        categoryRepository.delete(category);
    }
}