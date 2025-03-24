package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.dto.request.BudgetRequest;
import com.personalfinance.finance_tracker.dto.response.BudgetResponse;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping
    public ResponseEntity<List<BudgetResponse>> getAllBudgets(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<BudgetResponse> budgets = budgetService.getAllBudgetsByUser(userDetails.getId());
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/period/{year}/{month}")
    public ResponseEntity<List<BudgetResponse>> getBudgetsByPeriod(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        YearMonth period = YearMonth.of(year, month);
        List<BudgetResponse> budgets = budgetService.getBudgetsByPeriod(userDetails.getId(), period);
        return ResponseEntity.ok(budgets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetResponse> getBudgetById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BudgetResponse budget = budgetService.getBudgetById(id, userDetails.getId());
        return ResponseEntity.ok(budget);
    }

    @PostMapping
    public ResponseEntity<BudgetResponse> createBudget(
            @Valid @RequestBody BudgetRequest budgetRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BudgetResponse createdBudget = budgetService.createBudget(budgetRequest, userDetails.getId());
        return ResponseEntity.ok(createdBudget);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetResponse> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest budgetRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BudgetResponse updatedBudget = budgetService.updateBudget(id, budgetRequest, userDetails.getId());
        return ResponseEntity.ok(updatedBudget);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBudget(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        budgetService.deleteBudget(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}