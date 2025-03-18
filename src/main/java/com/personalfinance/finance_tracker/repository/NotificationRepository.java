package com.personalfinance.finance_tracker.repository;

import com.personalfinance.finance_tracker.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<Notification> findByUserIdAndReadOrderByCreatedAtDesc(Long userId, boolean read);

    Optional<Notification> findByIdAndUserId(Long id, Long userId);

    int countByUserIdAndRead(Long userId, boolean read);

}