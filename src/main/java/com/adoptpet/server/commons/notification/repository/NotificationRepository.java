package com.adoptpet.server.commons.notification.repository;

import com.adoptpet.server.commons.notification.domain.Notification;
import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiver(Member receiver);

    @Query(value = "SELECT * FROM NOTIFICATION n WHERE (n.url, n.reg_date) " +
            "IN (SELECT url, max(reg_date) " +
            "FROM NOTIFICATION " +
            "WHERE type = 'CHAT' OR type = 'NOTE' " +
            "GROUP BY url)",nativeQuery = true)
    List<Notification> findChatOrNoteByReceiver(Member member);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.sender.memberNo = 0 WHERE n.sender.memberNo = :memberNo")
    void deleteAllByMemberNo(Integer memberNo);
}
