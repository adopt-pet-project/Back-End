package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT n FROM Note n WHERE n.createMember = :memberNo OR n.joinMember = :memberNo")
    List<Note> findAllByMemberNo(@Param("memberNo") Integer memberNo);


    @Query("SELECT n FROM Note n " +
            "WHERE (n.createMember = :senderNo AND n.joinMember = :receiverNo)" +
            "OR (n.createMember = :receiverNo AND n.joinMember = :senderNo)")
    Optional<Note> findBySenderAndReceiver(@Param("senderNo") Integer senderNo,
                                          @Param("receiverNo") Integer receiverNo);
}
