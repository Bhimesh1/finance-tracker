package com.personalfinance.finance_tracker.controller;

import com.personalfinance.finance_tracker.dto.response.NotificationResponse;
import com.personalfinance.finance_tracker.security.service.UserDetailsImpl;
import com.personalfinance.finance_tracker.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<NotificationResponse> notifications = notificationService.getAllNotifications(userDetails.getId());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadNotifications(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<NotificationResponse> unreadNotifications = notificationService.getUnreadNotifications(userDetails.getId());
        return ResponseEntity.ok(unreadNotifications);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        int count = notificationService.getUnreadCount(userDetails.getId());
        return ResponseEntity.ok(Map.of("count", count));
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        NotificationResponse notification = notificationService.markAsRead(id, userDetails.getId());
        return ResponseEntity.ok(notification);
    }

    @PatchMapping("/read-all")
    public ResponseEntity<?> markAllAsRead(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok().build();
    }
}