package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.dto.request.FinancialGoalRequest;
import com.personalfinance.finance_tracker.dto.response.FinancialGoalResponse;
import com.personalfinance.finance_tracker.model.FinancialGoal;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.FinancialGoalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/goals")
public class FinancialGoalController {

    @Autowired
    private FinancialGoalService goalService;

    @GetMapping
    public ResponseEntity<List<FinancialGoalResponse>> getAllGoals(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<FinancialGoalResponse> goals = goalService.getAllGoalsByUser(userDetails.getId());
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<FinancialGoalResponse>> getGoalsByStatus(
            @PathVariable FinancialGoal.GoalStatus status,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<FinancialGoalResponse> goals = goalService.getGoalsByStatus(userDetails.getId(), status);
        return ResponseEntity.ok(goals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FinancialGoalResponse> getGoalById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FinancialGoalResponse goal = goalService.getGoalById(id, userDetails.getId());
        return ResponseEntity.ok(goal);
    }

    @PostMapping
    public ResponseEntity<FinancialGoalResponse> createGoal(
            @Valid @RequestBody FinancialGoalRequest goalRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FinancialGoalResponse createdGoal = goalService.createGoal(goalRequest, userDetails.getId());
        return ResponseEntity.ok(createdGoal);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FinancialGoalResponse> updateGoal(
            @PathVariable Long id,
            @Valid @RequestBody FinancialGoalRequest goalRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FinancialGoalResponse updatedGoal = goalService.updateGoal(id, goalRequest, userDetails.getId());
        return ResponseEntity.ok(updatedGoal);
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<FinancialGoalResponse> updateGoalProgress(
            @PathVariable Long id,
            @RequestParam BigDecimal amount,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        FinancialGoalResponse updatedGoal = goalService.updateGoalProgress(id, userDetails.getId(), amount);
        return ResponseEntity.ok(updatedGoal);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGoal(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        goalService.deleteGoal(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}