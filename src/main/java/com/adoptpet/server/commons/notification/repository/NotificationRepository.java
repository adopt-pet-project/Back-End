package com.adoptpet.server.commons.notification.repository;

import com.adoptpet.server.commons.notification.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

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
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateAllBySenderNo(Integer memberNo);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Notification n WHERE n.receiver.memberNo = :memberNo")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void deleteAllByReceiverNo(Integer memberNo);
}
