package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.model.Transaction;
import com.personalfinance.finance_tracker.repository.TransactionRepository;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.ExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/export")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @Autowired
    private TransactionRepository transactionRepository;

    @GetMapping("/transactions/csv")
    public ResponseEntity<byte[]> exportTransactionsToCSV(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        List<Transaction> transactions = transactionRepository.findByUserId(userDetails.getId()).stream()
                .filter(t -> !t.getTransactionDate().isBefore(startDateTime) && !t.getTransactionDate().isAfter(endDateTime))
                .collect(Collectors.toList());

        byte[] csvData = exportService.exportTransactionsToCSV(transactions);

        String filename = "transactions_" + startDate.format(DateTimeFormatter.ISO_DATE) +
                "_to_" + endDate.format(DateTimeFormatter.ISO_DATE) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(csvData.length)
                .body(csvData);
    }

    @GetMapping("/reports/monthly/{year}/{month}")
    public ResponseEntity<byte[]> generateMonthlyReport(
            @PathVariable int year,
            @PathVariable int month,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        byte[] reportData = exportService.generateMonthlyReport(userDetails.getId(), year, month);

        String monthName = month < 10 ? "0" + month : String.valueOf(month);
        String filename = "monthly_report_" + year + "_" + monthName + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(reportData.length)
                .body(reportData);
    }

    @GetMapping("/reports/transactions")
    public ResponseEntity<byte[]> generateTransactionReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        byte[] reportData = exportService.generateTransactionReport(userDetails.getId(), startDate, endDate);

        String filename = "transaction_report_" + startDate.format(DateTimeFormatter.ISO_DATE) +
                "_to_" + endDate.format(DateTimeFormatter.ISO_DATE) + ".csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(reportData.length)
                .body(reportData);
    }
}