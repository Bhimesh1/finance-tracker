package com.personalfinance.finance_tracker.service;

import com.personalfinance.finance_tracker.model.Transaction;

import java.time.LocalDate;
import java.util.List;

public interface ExportService {

    byte[] exportTransactionsToCSV(List<Transaction> transactions);

    byte[] generateMonthlyReport(Long userId, int year, int month);

    byte[] generateTransactionReport(Long userId, LocalDate startDate, LocalDate endDate);

}