package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.dto.response.AccountBalanceHistoryResponse;
import com.personalfinance.finance_tracker.dto.response.CashFlowResponse;
import com.personalfinance.finance_tracker.dto.response.ExpenseByCategoryResponse;
import com.personalfinance.finance_tracker.dto.response.MonthlySummaryResponse;
import com.personalfinance.finance_tracker.exception.ResourceNotFoundException;
import com.personalfinance.finance_tracker.model.Account;
import com.personalfinance.finance_tracker.model.Category;
import com.personalfinance.finance_tracker.model.FinancialGoal;
import com.personalfinance.finance_tracker.model.Transaction;
import com.personalfinance.finance_tracker.repository.AccountRepository;
import com.personalfinance.finance_tracker.repository.BudgetRepository;
import com.personalfinance.finance_tracker.repository.CategoryRepository;
import com.personalfinance.finance_tracker.repository.FinancialGoalRepository;
import com.personalfinance.finance_tracker.repository.TransactionRepository;
import com.personalfinance.finance_tracker.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private FinancialGoalRepository goalRepository;

    @Override
    public List<ExpenseByCategoryResponse> getExpensesByCategory(Long userId, LocalDate startDate, LocalDate endDate) {
        // Convert LocalDate to LocalDateTime for filtering
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.atTime(LocalTime.MAX);

        // Get all expense transactions for the period
        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .filter(t -> !t.getTransactionDate().isBefore(start) && !t.getTransactionDate().isAfter(end))
                .collect(Collectors.toList());

        // Calculate total expenses
        BigDecimal totalExpenses = transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group by category and sum amounts
        Map<Long, BigDecimal> expensesByCategory = new HashMap<>();
        Map<Long, String> categoryNames = new HashMap<>();
        Map<Long, String> categoryColors = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getCategory() != null) {
                Long categoryId = transaction.getCategory().getId();
                BigDecimal amount = transaction.getAmount();

                // Add amount to category total
                expensesByCategory.put(
                        categoryId,
                        expensesByCategory.getOrDefault(categoryId, BigDecimal.ZERO).add(amount)
                );

                // Store category name and color
                categoryNames.put(categoryId, transaction.getCategory().getName());
                categoryColors.put(categoryId, transaction.getCategory().getColor());
            }
        }

        // Create response objects
        List<ExpenseByCategoryResponse> result = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : expensesByCategory.entrySet()) {
            Long categoryId = entry.getKey();
            BigDecimal amount = entry.getValue();

            result.add(new ExpenseByCategoryResponse(
                    categoryId,
                    categoryNames.get(categoryId),
                    categoryColors.get(categoryId),
                    amount,
                    totalExpenses
            ));
        }

        // Sort by amount in descending order
        result.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));

        return result;
    }

    @Override
    public Map<String, Object> getDashboardSummary(Long userId) {
        Map<String, Object> summary = new HashMap<>();

        // Get current month
        YearMonth currentMonth = YearMonth.now();

        // Get cash flow for current month
        CashFlowResponse cashFlow = getCashFlowForMonth(userId, currentMonth);

        // Get transactions for current month
        LocalDateTime startOfMonth = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = currentMonth.atEndOfMonth().atTime(LocalTime.MAX);

        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(t -> !t.getTransactionDate().isBefore(startOfMonth) && !t.getTransactionDate().isAfter(endOfMonth))
                .collect(Collectors.toList());

        // Get account balances
        List<Account> accounts = accountRepository.findByUserId(userId);

        BigDecimal totalBalance = accounts.stream()
                .map(Account::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Get active budget count
        int activeBudgets = budgetRepository.findByUserIdAndPeriod(userId, currentMonth).size();

        // Get active goals count
        int activeGoals = goalRepository.findByUserIdAndStatus(userId, FinancialGoal.GoalStatus.IN_PROGRESS).size();

        // Get transaction counts
        int transactionsThisMonth = transactions.size();

        // Get popular categories
        Map<Long, Long> categoryCounts = transactions.stream()
                .filter(t -> t.getCategory() != null)
                .collect(Collectors.groupingBy(t -> t.getCategory().getId(), Collectors.counting()));

        List<Map.Entry<Long, Long>> topCategories = categoryCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        List<Map<String, Object>> topCategoriesInfo = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : topCategories) {
            Category category = categoryRepository.findById(entry.getKey()).orElse(null);
            if (category != null) {
                Map<String, Object> categoryInfo = new HashMap<>();
                categoryInfo.put("id", category.getId());
                categoryInfo.put("name", category.getName());
                categoryInfo.put("count", entry.getValue());
                categoryInfo.put("color", category.getColor());
                topCategoriesInfo.add(categoryInfo);
            }
        }

        // Add to summary
        summary.put("totalBalance", totalBalance);
        summary.put("monthlyIncome", cashFlow.getTotalIncome());
        summary.put("monthlyExpense", cashFlow.getTotalExpense());
        summary.put("monthlySavings", cashFlow.getNetCashFlow());
        summary.put("activeBudgetsCount", activeBudgets);
        summary.put("activeGoalsCount", activeGoals);
        summary.put("transactionsCount", transactionsThisMonth);
        summary.put("topCategories", topCategoriesInfo);
        summary.put("accountsCount", accounts.size());

        return summary;
    }


    @Override
    public CashFlowResponse getCashFlowForMonth(Long userId, YearMonth month) {
        // Convert YearMonth to LocalDateTime for filtering
        LocalDateTime start = month.atDay(1).atStartOfDay();
        LocalDateTime end = month.atEndOfMonth().atTime(LocalTime.MAX);

        // Get all transactions for the month
        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(t -> !t.getTransactionDate().isBefore(start) && !t.getTransactionDate().isAfter(end))
                .collect(Collectors.toList());

        // Separate income and expense transactions
        List<Transaction> incomeTransactions = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
                .collect(Collectors.toList());

        List<Transaction> expenseTransactions = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .collect(Collectors.toList());

        // Calculate totals
        BigDecimal totalIncome = incomeTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = expenseTransactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Group by category and sum amounts
        List<CashFlowResponse.CashFlowItemResponse> incomeItems = summarizeByCategory(incomeTransactions);
        List<CashFlowResponse.CashFlowItemResponse> expenseItems = summarizeByCategory(expenseTransactions);

        // Sort items by amount in descending order
        incomeItems.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));
        expenseItems.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));

        return new CashFlowResponse(month, totalIncome, totalExpense, incomeItems, expenseItems);
    }

    @Override
    public MonthlySummaryResponse getMonthlySummary(Long userId, YearMonth startMonth, YearMonth endMonth) {
        List<MonthlySummaryResponse.MonthlyDataPoint> dataPoints = new ArrayList<>();

        // Generate a list of all months in the range
        List<YearMonth> months = new ArrayList<>();
        YearMonth current = startMonth;
        while (!current.isAfter(endMonth)) {
            months.add(current);
            current = current.plusMonths(1);
        }

        // Process each month
        for (YearMonth month : months) {
            // Get cash flow for the month
            CashFlowResponse cashFlow = getCashFlowForMonth(userId, month);

            // Create data point
            MonthlySummaryResponse.MonthlyDataPoint dataPoint =
                    new MonthlySummaryResponse.MonthlyDataPoint(month, cashFlow.getTotalIncome(), cashFlow.getTotalExpense());

            dataPoints.add(dataPoint);
        }

        return new MonthlySummaryResponse(dataPoints);
    }

    @Override
    public AccountBalanceHistoryResponse getAccountBalanceHistory(Long userId, Long accountId, LocalDate startDate, LocalDate endDate) {
        // Verify account exists and belongs to user
        Account account = accountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));

        // Get current balance
        BigDecimal currentBalance = account.getBalance();

        // Get all transactions for this account in reverse chronological order
        List<Transaction> transactions = transactionRepository.findByUserIdAndAccountId(userId, accountId).stream()
                .filter(t -> !t.getTransactionDate().toLocalDate().isAfter(endDate))
                .sorted(Comparator.comparing(Transaction::getTransactionDate).reversed())
                .collect(Collectors.toList());

        // Generate daily balance points
        List<AccountBalanceHistoryResponse.BalancePoint> balancePoints = new ArrayList<>();

        // If no transactions or all transactions are after the end date, just use current balance
        if (transactions.isEmpty()) {
            balancePoints.add(new AccountBalanceHistoryResponse.BalancePoint(endDate, currentBalance));
            return new AccountBalanceHistoryResponse(accountId, account.getName(), balancePoints);
        }

        // Start with current balance and work backwards
        BigDecimal runningBalance = currentBalance;
        Map<LocalDate, BigDecimal> dailyBalances = new HashMap<>();

        // Process transactions in reverse chronological order to calculate historical balances
        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getTransactionDate().toLocalDate();

            // Adjust running balance based on transaction type
            if (transaction.getType() == Transaction.TransactionType.INCOME) {
                runningBalance = runningBalance.subtract(transaction.getAmount());
            } else {
                runningBalance = runningBalance.add(transaction.getAmount());
            }

            // Store balance for this day if we haven't seen it yet
            dailyBalances.putIfAbsent(transactionDate, runningBalance);
        }

        // Create balance points for date range
        LocalDate current = startDate;
        BigDecimal lastKnownBalance = transactions.get(transactions.size() - 1).getType() == Transaction.TransactionType.INCOME
                ? runningBalance.subtract(transactions.get(transactions.size() - 1).getAmount())
                : runningBalance.add(transactions.get(transactions.size() - 1).getAmount());

        while (!current.isAfter(endDate)) {
            BigDecimal balance = dailyBalances.getOrDefault(current, lastKnownBalance);
            balancePoints.add(new AccountBalanceHistoryResponse.BalancePoint(current, balance));
            lastKnownBalance = balance;
            current = current.plusDays(1);
        }

        // Sort by date
        balancePoints.sort(Comparator.comparing(AccountBalanceHistoryResponse.BalancePoint::getDate));

        return new AccountBalanceHistoryResponse(accountId, account.getName(), balancePoints);
    }

    /**
     * Helper method to summarize transactions by category
     */
    private List<CashFlowResponse.CashFlowItemResponse> summarizeByCategory(List<Transaction> transactions) {
        // Group by category and sum amounts
        Map<Long, BigDecimal> amountsByCategory = new HashMap<>();
        Map<Long, String> categoryNames = new HashMap<>();
        Map<Long, String> categoryColors = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getCategory() != null) {
                Long categoryId = transaction.getCategory().getId();
                BigDecimal amount = transaction.getAmount();

                // Add amount to category total
                amountsByCategory.put(
                        categoryId,
                        amountsByCategory.getOrDefault(categoryId, BigDecimal.ZERO).add(amount)
                );

                // Store category name and color
                categoryNames.put(categoryId, transaction.getCategory().getName());
                categoryColors.put(categoryId, transaction.getCategory().getColor());
            }
        }

        // Create cash flow items
        List<CashFlowResponse.CashFlowItemResponse> items = new ArrayList<>();
        for (Map.Entry<Long, BigDecimal> entry : amountsByCategory.entrySet()) {
            Long categoryId = entry.getKey();
            BigDecimal amount = entry.getValue();

            items.add(new CashFlowResponse.CashFlowItemResponse(
                    categoryId,
                    categoryNames.get(categoryId),
                    categoryColors.get(categoryId),
                    amount
            ));
        }

        return items;
    }
}