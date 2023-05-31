package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Integer> {

    @Query("SELECT n FROM Note n WHERE n.createMember = :memberNo OR n.joinMember = :memberNo")
    List<Note> findAllByMemberNo(@Param("memberNo") Integer memberNo);
}
