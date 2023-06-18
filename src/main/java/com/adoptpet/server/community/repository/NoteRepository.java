package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT n FROM Note n WHERE n.createMember = :memberNo OR n.joinMember = :memberNo ORDER BY n.regDate desc")
    List<Note> findAllByMemberNo(@Param("memberNo") Integer memberNo);


    @Query("SELECT n FROM Note n " +
            "WHERE (n.createMember = :senderNo AND n.joinMember = :receiverNo)" +
            "OR (n.createMember = :receiverNo AND n.joinMember = :senderNo)")
    Optional<Note> findBySenderAndReceiver(@Param("senderNo") Integer senderNo,
                                          @Param("receiverNo") Integer receiverNo);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Note n SET n.createMember = 0 WHERE n.createMember = :memberNo")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateCreateMemberByDeletion(@Param("memberNo") Integer memberNo);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Note n SET n.joinMember = 0 WHERE n.joinMember = :memberNo")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void updateJoinMemberByDeletion(@Param("memberNo") Integer memberNo);
}
