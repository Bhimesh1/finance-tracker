package com.personalfinance.finance_tracker.dto.response;

import com.personalfinance.finance_tracker.model.Notification;

import java.time.LocalDateTime;

public class NotificationResponse {

    private Long id;
    private String message;
    private Notification.NotificationType type;
    private boolean read;
    private LocalDateTime createdAt;

    // Default constructor
    public NotificationResponse() {
    }

    // Constructor from Notification entity
    public NotificationResponse(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.read = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }

    // All-args constructor
    public NotificationResponse(Long id, String message, Notification.NotificationType type,
                                boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.read = read;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Notification.NotificationType getType() {
        return type;
    }

    public void setType(Notification.NotificationType type) {
        this.type = type;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}