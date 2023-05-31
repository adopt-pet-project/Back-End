package com.adoptpet.server.commons.notification.repository;

import com.adoptpet.server.commons.notification.domain.Notification;
import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiver(Member receiver);
}
