package com.adoptpet.server.community.repository;

import com.adoptpet.server.community.domain.Note;
import com.adoptpet.server.community.domain.NoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface NoteHistoryRepository extends JpaRepository<NoteHistory, Integer> {

    NoteHistory findTop1ByNoteOrderByRegDateDesc(Note note);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE NOTE_HISTORY nh SET nh.read_status = 1 " +
            "WHERE nh.note_no = :noteNo AND nh.receiver_no = :memberNo", nativeQuery = true)
    void updateReadStatusAllByNoteNo(@Param("noteNo") Integer noteNo,
                                     @Param("memberNo") Integer memberNo);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE NoteHistory nh SET nh.receiverDel = 2, nh.receiverNo = 0 WHERE nh.receiverNo = :memberNo")
    void deleteHistoryByReceiverNo(@Param("memberNo") Integer memberNo);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE NoteHistory nh SET nh.senderDel = 2, nh.senderNo = 0 WHERE nh.senderNo = :memberNo")
    void deleteHistoryBySenderNo(@Param("memberNo") Integer memberNo);
}
