package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Note;
import com.adoptpet.server.community.domain.NoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface NoteHistoryRepository extends JpaRepository<NoteHistory, Integer> {

    NoteHistory findTop1ByNoteOrderByRegDateDesc(Note note);

    @Query("SELECT nh FROM NoteHistory nh WHERE nh.note.noteNo = :noteNo")
    List<NoteHistory> findAllByNoteNo(@Param("noteNo") Integer noteNo);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE NOTE_HISTORY nh SET nh.read_status = 1 " +
            "WHERE nh.note_no = :noteNo AND nh.receiver_no = :memberNo",
            nativeQuery = true)
    void updateReadStatusAllByNoteNo(@Param("noteNo") Integer noteNo,
                                     @Param("memberNo") Integer memberNo);
}
