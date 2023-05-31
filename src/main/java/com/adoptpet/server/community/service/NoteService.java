package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.community.domain.Note;
import com.adoptpet.server.community.domain.NoteHistory;
import com.adoptpet.server.community.repository.NoteHistoryRepository;
import com.adoptpet.server.community.repository.NoteRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class NoteService {


    private final NoteRepository noteRepository;
    private final NoteHistoryRepository noteHistoryRepository;
    private final MemberService memberService;
    private final NotificationService notificationService;

    @Transactional
    public void sendNote(SecurityUserDto loginMember, Integer receiverNo, String content){

        // 받는 사람 조회
        Member receiver = memberService.findByMemberNo(receiverNo);
        // 쪽지 발신 내역 생성
        NoteHistory noteHistory
                = NoteHistory.createNoteHistory(loginMember.getMemberNo(), receiver.getMemberNo(), content);
        // 쪽지방 생성
        Note note = Note.createNote(loginMember.getMemberNo(), receiver.getMemberNo(), noteHistory);
        // 저장
        noteRepository.save(note);
        // 알림 전송
    }

    @Transactional(readOnly = true)
    public void readNoteHistoryList(SecurityUserDto loginMember, Integer noteNo){

    }

    @Transactional(readOnly = true)
    public void readNoteList(SecurityUserDto loginMember){

    }

    @Transactional
    public void updateNoteHistory(Integer noteNo){

    }

    @Transactional
    public void deleteNoteHistory(Integer noteHistoryNo){

    }


    private Note findNoteById(Integer noteNo){
        return noteRepository.findById(noteNo).orElseThrow(ErrorCode::throwNoteNotFound);
    }
}
