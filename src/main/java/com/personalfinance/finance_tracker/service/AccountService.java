package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.dto.request.AccountRequest;
import com.personalfinance.finance_tracker.dto.response.AccountResponse;

import java.util.List;

public interface AccountService {

    List<AccountResponse> getAllAccountsByUser(Long userId);

    AccountResponse getAccountById(Long id, Long userId);

    AccountResponse createAccount(AccountRequest accountRequest, Long userId);

    AccountResponse updateAccount(Long id, AccountRequest accountRequest, Long userId);

    void deleteAccount(Long id, Long userId);

}