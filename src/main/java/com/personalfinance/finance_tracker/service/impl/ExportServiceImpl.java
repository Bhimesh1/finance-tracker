package com.personalfinance.finance_tracker.service.impl;

import com.personalfinance.finance_tracker.model.Transaction;
import com.personalfinance.finance_tracker.repository.TransactionRepository;
import com.personalfinance.finance_tracker.service.AnalyticsService;
import com.personalfinance.finance_tracker.service.ExportService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExportServiceImpl implements ExportService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AnalyticsService analyticsService;

    @Override
    public byte[] exportTransactionsToCSV(List<Transaction> transactions) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Date", "Description", "Category", "Account", "Type", "Amount", "Notes"))) {

            for (Transaction transaction : transactions) {
                csvPrinter.printRecord(
                        transaction.getTransactionDate().format(DateTimeFormatter.ISO_DATE),
                        transaction.getDescription(),
                        transaction.getCategory() != null ? transaction.getCategory().getName() : "None",
                        transaction.getAccount().getName(),
                        transaction.getType().name(),
                        transaction.getAmount(),
                        transaction.getNotes()
                );
            }

            csvPrinter.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to export data to CSV", e);
        }
    }

    @Override
    public byte[] generateMonthlyReport(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

        // Get transactions for the month
        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(t -> !t.getTransactionDate().isBefore(startDateTime) && !t.getTransactionDate().isAfter(endDateTime))
                .sorted((a, b) -> a.getTransactionDate().compareTo(b.getTransactionDate()))
                .collect(Collectors.toList());

        // Calculate summary data
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netAmount = totalIncome.subtract(totalExpense);

        // Generate CSV
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            // Add title
            csvPrinter.printRecord("Monthly Financial Report - " + yearMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
            csvPrinter.println();

            // Add summary
            csvPrinter.printRecord("Summary");
            csvPrinter.printRecord("Total Income", totalIncome);
            csvPrinter.printRecord("Total Expenses", totalExpense);
            csvPrinter.printRecord("Net Amount", netAmount);
            csvPrinter.println();

            // Add transactions header
            csvPrinter.printRecord("Transactions");
            csvPrinter.printRecord("Date", "Description", "Category", "Account", "Type", "Amount", "Notes");

            // Add transaction data
            for (Transaction transaction : transactions) {
                csvPrinter.printRecord(
                        transaction.getTransactionDate().format(DateTimeFormatter.ISO_DATE),
                        transaction.getDescription(),
                        transaction.getCategory() != null ? transaction.getCategory().getName() : "None",
                        transaction.getAccount().getName(),
                        transaction.getType().name(),
                        transaction.getAmount(),
                        transaction.getNotes()
                );
            }

            csvPrinter.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate monthly report", e);
        }
    }

    @Override
    public byte[] generateTransactionReport(Long userId, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        // Get transactions for the date range
        List<Transaction> transactions = transactionRepository.findByUserId(userId).stream()
                .filter(t -> !t.getTransactionDate().isBefore(startDateTime) && !t.getTransactionDate().isAfter(endDateTime))
                .sorted((a, b) -> a.getTransactionDate().compareTo(b.getTransactionDate()))
                .collect(Collectors.toList());

        // Calculate summary data
        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netAmount = totalIncome.subtract(totalExpense);

        // Generate CSV
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {

            // Add title
            csvPrinter.printRecord("Transaction Report: " +
                    startDate.format(DateTimeFormatter.ISO_DATE) + " to " +
                    endDate.format(DateTimeFormatter.ISO_DATE));
            csvPrinter.println();

            // Add summary
            csvPrinter.printRecord("Summary");
            csvPrinter.printRecord("Total Income", totalIncome);
            csvPrinter.printRecord("Total Expenses", totalExpense);
            csvPrinter.printRecord("Net Amount", netAmount);
            csvPrinter.println();

            // Add transactions header
            csvPrinter.printRecord("Transactions");
            csvPrinter.printRecord("Date", "Description", "Category", "Account", "Type", "Amount", "Notes");

            // Add transaction data
            for (Transaction transaction : transactions) {
                csvPrinter.printRecord(
                        transaction.getTransactionDate().format(DateTimeFormatter.ISO_DATE),
                        transaction.getDescription(),
                        transaction.getCategory() != null ? transaction.getCategory().getName() : "None",
                        transaction.getAccount().getName(),
                        transaction.getType().name(),
                        transaction.getAmount(),
                        transaction.getNotes()
                );
            }

            csvPrinter.flush();
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate transaction report", e);
        }
    }
}