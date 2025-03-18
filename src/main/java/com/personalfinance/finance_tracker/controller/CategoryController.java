package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.dto.request.CategoryRequest;
import com.personalfinance.finance_tracker.dto.response.CategoryResponse;
import com.personalfinance.finance_tracker.model.Category;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CategoryResponse> categories = categoryService.getAllCategoriesByUser(userDetails.getId());
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByType(
            @PathVariable Category.CategoryType type,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<CategoryResponse> categories = categoryService.getCategoriesByType(userDetails.getId(), type);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CategoryResponse category = categoryService.getCategoryById(id, userDetails.getId());
        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CategoryResponse createdCategory = categoryService.createCategory(categoryRequest, userDetails.getId());
        return ResponseEntity.ok(createdCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, categoryRequest, userDetails.getId());
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        categoryService.deleteCategory(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}