package com.adoptpet.server.commons.notification.repository;

import com.adoptpet.server.commons.notification.domain.Notification;
import com.adoptpet.server.user.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByReceiver(Member receiver);

    @Query(value = "SELECT * FROM NOTIFICATION n " +
            "WHERE (n.url, n.reg_date) " +
            "IN (SELECT url, max(reg_date) " +
            "FROM NOTIFICATION " +
            "WHERE type = 'CHAT' OR type = 'NOTE' " +
            "GROUP BY url) " +
            "AND n.receiver_no = :memberNo",nativeQuery = true)
    List<Notification> findChatOrNoteByReceiver(Integer memberNo);

    @Query("SELECT n FROM Notification n " +
            "WHERE NOT(n.type = 'CHAT' OR n.type ='NOTE') " +
            "AND n.receiver.memberNo = :memberNo")
    List<Notification> findOtherByReceiver(@Param("memberNo") Integer memberNo);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Notification n SET n.sender.memberNo = 0 WHERE n.sender.memberNo = :memberNo")
    void deleteAllByMemberNo(Integer memberNo);
}
