package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.community.domain.Note;
import com.adoptpet.server.community.domain.NoteHistory;
import com.adoptpet.server.community.dto.NoteDto;
import com.adoptpet.server.community.repository.NoteHistoryRepository;
import com.adoptpet.server.community.repository.NoteRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

        final Integer senderNo = loginMember.getMemberNo();

        // 받는 사람 조회
        Member receiver = memberService.findByMemberNo(receiverNo);
        // 쪽지 발신 내역 생성
        NoteHistory noteHistory
                = NoteHistory.createNoteHistory(senderNo, receiver.getMemberNo(), content);
        // 쪽지방 생성
        Note note = Note.createNote(loginMember.getMemberNo(), receiver.getMemberNo(), noteHistory);
        // 저장
        Note savedNote = noteRepository.save(note);
        // 알림 전송을 위해 보내는 사람 조회
        Member sender = memberService.findByMemberNo(senderNo);
        // 알림 전송
        notificationService.send(sender, receiver, NotifiTypeEnum.NOTE, savedNote.getNoteNo(), content);
    }

    @Transactional(readOnly = true)
    public List<NoteDto> readNoteList(SecurityUserDto loginMember){
        final Integer memberNo = loginMember.getMemberNo();
        List<NoteDto> noteDtoList = new ArrayList<>();

        // 쪽지방 조회
        List<Note> noteList = noteRepository.findAllByMemberNo(loginMember.getMemberNo());

        boolean isMine = false;
        String nickName;
        Member opponent;

        for(Note note :noteList){
            // 쪽지방 고유키로 쪽지 조회
            NoteHistory history = noteHistoryRepository.findTop1ByNoteOrderByRegDateDesc(note);
            // 조회를 요청한 회원의 상대방 조회
            if(!memberNo.equals(note.getCreateMember())){
                opponent = memberService.findByMemberNo(memberNo);
            } else {
                opponent = memberService.findByMemberNo(note.getJoinMember());
            }
            // 요청한 회원의 입장에서 받은 쪽지인지 보낸 쪽지인지 확인
            if(memberNo.equals(history.getSenderNo())){
                isMine = true;
            }
            // 상대방 닉네임 추가
            nickName = opponent.getNickname();

            NoteDto noteDto = NoteDto.builder()
                    .noteNo(note.getNoteNo())
                    .nickName(nickName)
                    .content(history.getContent())
                    .mine(isMine)
                    .regDate(history.getRegDate())
                    .readStatus(history.isRead())
                    .build();

            noteDtoList.add(noteDto);
        }
        return noteDtoList;
    }

    @Transactional(readOnly = true)
    public void readNoteHistoryList(SecurityUserDto loginMember, Integer noteNo){

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
