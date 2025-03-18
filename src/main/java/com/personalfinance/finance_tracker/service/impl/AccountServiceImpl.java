package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.dto.request.AccountRequest;
import com.personalfinance.finance_tracker.dto.response.AccountResponse;
import com.personalfinance.finance_tracker.exception.ResourceNotFoundException;
import com.personalfinance.finance_tracker.model.Account;
import com.personalfinance.finance_tracker.model.User;
import com.personalfinance.finance_tracker.repository.AccountRepository;
import com.personalfinance.finance_tracker.repository.UserRepository;
import com.personalfinance.finance_tracker.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<AccountResponse> getAllAccountsByUser(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse getAccountById(Long id, Long userId) {
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        return new AccountResponse(account);
    }

    @Override
    @Transactional
    public AccountResponse createAccount(AccountRequest accountRequest, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Account account = new Account();
        account.setName(accountRequest.getName());
        account.setDescription(accountRequest.getDescription());
        account.setType(accountRequest.getType());
        account.setBalance(accountRequest.getBalance());
        account.setInstitution(accountRequest.getInstitution());
        account.setUser(user);

        Account savedAccount = accountRepository.save(account);

        return new AccountResponse(savedAccount);
    }

    @Override
    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest accountRequest, Long userId) {
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        account.setName(accountRequest.getName());
        account.setDescription(accountRequest.getDescription());
        account.setType(accountRequest.getType());
        account.setBalance(accountRequest.getBalance());
        account.setInstitution(accountRequest.getInstitution());

        Account updatedAccount = accountRepository.save(account);

        return new AccountResponse(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id, Long userId) {
        Account account = accountRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        accountRepository.delete(account);
    }
}