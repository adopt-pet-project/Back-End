package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.community.domain.LogicalDelEnum;
import com.adoptpet.server.community.domain.Note;
import com.adoptpet.server.community.domain.NoteHistory;
import com.adoptpet.server.community.dto.NoteDto;
import com.adoptpet.server.community.dto.NoteHistoryDto;
import com.adoptpet.server.community.repository.NoteHistoryRepository;
import com.adoptpet.server.community.repository.NoteRepository;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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

        List<Note> noteList = noteRepository.findBySenderAndReceiver(senderNo, receiverNo);

        Integer refNo;

        if(noteList.isEmpty()){
            // 쪽지방 생성
            Note note = Note.createNote(loginMember.getMemberNo(), receiver.getMemberNo(), noteHistory);
            Note savedNote = noteRepository.save(note);
            refNo = savedNote.getNoteNo();
        } else {
            // 쪽지방 조회
            Note note = noteList.get(0);
            noteHistory.addNote(note);
            noteHistoryRepository.save(noteHistory);
            refNo = note.getNoteNo();
        }
        // 알림 전송을 위해 보내는 사람 조회
        Member sender = memberService.findByMemberNo(senderNo);
        // 알림 전송
        notificationService.send(sender, receiver, NotifiTypeEnum.NOTE, refNo, content);
    }


    @Transactional(readOnly = true)
    public List<NoteDto> readNoteList(SecurityUserDto loginMember){
        final Integer memberNo = loginMember.getMemberNo();

        // 쪽지방 조회
        List<Note> noteList = noteRepository.findAllByMemberNo(loginMember.getMemberNo());

        boolean isMine = false;
        String nickName;
        Member opponent;

        List<NoteDto> noteDtoList = new ArrayList<>();

        for(Note note : noteList){
            // 쪽지방 고유키로 쪽지 조회
            NoteHistory history = noteHistoryRepository.findTop1ByNoteOrderByRegDateDesc(note);
            // 조회를 요청한 회원의 상대방 조회
            if(memberNo.equals(note.getCreateMember())){
                opponent = memberService.findByMemberNo(note.getJoinMember());
            } else {
                opponent = memberService.findByMemberNo(note.getCreateMember());
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
    public List<NoteHistoryDto> readNoteHistoryList(SecurityUserDto loginMember, Integer noteNo){

        final Integer memberNo = loginMember.getMemberNo();
        List<NoteHistoryDto> noteHistoryDtoList = new ArrayList<>();

        List<NoteHistory> noteHistoryList = noteHistoryRepository.findAllByNoteNo(noteNo);

        boolean isMine;
        LogicalDelEnum logicalDel;
        for(NoteHistory noteHistory : noteHistoryList){

            // 요청한 회원이 보낸 쪽지는 true, 받은 쪽지는 false
            if(memberNo.equals(noteHistory.getSenderNo())){
                isMine = true;
                logicalDel = noteHistory.getSenderDel();
            } else {
                isMine = false;
                logicalDel = noteHistory.getReceiverDel();
            }

            NoteHistoryDto noteHistoryDto = NoteHistoryDto.builder()
                    .historyNo(noteHistory.getHistoryNo())
                    .mine(isMine)
                    .content(noteHistory.getContent())
                    .regDate(noteHistory.getRegDate())
                    .logicalDel(logicalDel)
                    .build();

            noteHistoryDtoList.add(noteHistoryDto);
        }
        return noteHistoryDtoList;
    }

    @Transactional
    public void updateNoteHistory(SecurityUserDto loginMember,Integer noteNo){

        // 쪽지 조회
        Note note = findNoteById(noteNo);

        // 쪽지방에 해당하는 쪽지 중 받는 사람이 요청한 회원인 쪽지 모두 읽음 처리
        noteHistoryRepository.updateReadStatusAllByNoteNo(note.getNoteNo(),loginMember.getMemberNo());
    }

    @Transactional
    public void deleteNoteHistory(SecurityUserDto loginMember,Integer historyNo){
        final Integer memberNo = loginMember.getMemberNo();

        // 쪽지 내역 조회
        NoteHistory noteHistory = noteHistoryRepository.findById(historyNo)
                .orElseThrow(ErrorCode::throwNoteHistoryNotFound);

        // 논리 삭제
        noteHistory.softDeleteNoteHistory(memberNo,LogicalDelEnum.DELETE);
        noteHistoryRepository.save(noteHistory);
    }


    private Note findNoteById(Integer noteNo){
        return noteRepository.findById(noteNo).orElseThrow(ErrorCode::throwNoteNotFound);
    }
}
