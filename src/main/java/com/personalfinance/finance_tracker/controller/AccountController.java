package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.dto.request.AccountRequest;
import com.personalfinance.finance_tracker.dto.response.AccountResponse;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AccountResponse> accounts = accountService.getAllAccountsByUser(userDetails.getId());
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AccountResponse account = accountService.getAccountById(id, userDetails.getId());
        return ResponseEntity.ok(account);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody AccountRequest accountRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AccountResponse createdAccount = accountService.createAccount(accountRequest, userDetails.getId());
        return ResponseEntity.ok(createdAccount);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountRequest accountRequest,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        AccountResponse updatedAccount = accountService.updateAccount(id, accountRequest, userDetails.getId());
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        accountService.deleteAccount(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}