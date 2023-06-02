package com.adoptpet.server.community.service;

import com.adoptpet.server.commons.exception.CustomException;
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
import java.util.Optional;

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
        NoteHistory history
                = NoteHistory.createNoteHistory(senderNo, receiver.getMemberNo(), content);

        Optional<Note> note = noteRepository.findBySenderAndReceiver(senderNo, receiverNo);
        Integer refNo;

        // 쪽지방 존재 여부에 따른 분기 처리
        if(note.isEmpty()){
            // 쪽지방 생성
            Note createdTote = Note.createNote(loginMember.getMemberNo(), receiver.getMemberNo(), history);
            Note savedNote = noteRepository.save(createdTote);
            refNo = savedNote.getNoteNo();
        } else {
            // 쪽지방 조회
            history.addNote(note.get());
            noteHistoryRepository.save(history);
            refNo = note.get().getNoteNo();
        }
        // 알림 전송을 위해 보내는 사람 조회
        Member sender = memberService.findByMemberNo(senderNo);
        // 알림 전송
        notificationService.send(sender, receiver, NotifiTypeEnum.NOTE, refNo, content);
    }


    @Transactional(readOnly = true)
    public List<NoteDto> readNoteList(SecurityUserDto loginMember){
        final Integer memberNo = loginMember.getMemberNo();
        List<NoteDto> noteDtoList = new ArrayList<>();

        // 쪽지방 조회
        List<Note> noteList = noteRepository.findAllByMemberNo(loginMember.getMemberNo());

        for(Note note : noteList){
            // 쪽지방 고유키로 쪽지 조회
            NoteHistory history = noteHistoryRepository
                    .findTop1ByNoteOrderByRegDateDesc(note);

            // 조회를 요청한 회원의 상대방 조회
            Integer opponentNo;
            if(memberNo.equals(note.getCreateMember())){
                opponentNo = note.getJoinMember();
            } else {
                opponentNo = note.getCreateMember();
            }

            // 상대방 닉네임 추가
            Member opponent = memberService.findByMemberNo(opponentNo);
            String nickName = opponent.getNickname();

            // 요청한 회원이 보낸 쪽지는 true, 받은 쪽지는 false
            boolean isMine = memberNo.equals(history.getSenderNo());

            // 삭제 상태 추가
            LogicalDelEnum logicalDel;
            if(isMine){
                logicalDel = history.getSenderDel();
            } else {
                logicalDel = history.getReceiverDel();
            }

            NoteDto noteDto = NoteDto.builder()
                    .noteNo(note.getNoteNo())
                    .nickName(nickName)
                    .content(history.getContent())
                    .mine(isMine)
                    .regDate(history.getRegDate())
                    .readStatus(history.isRead())
                    .logicalDel(logicalDel)
                    .build();

            noteDtoList.add(noteDto);
        }
        return noteDtoList;
    }

    @Transactional(readOnly = true)
    public List<NoteHistoryDto> readNoteHistoryList(SecurityUserDto loginMember, Integer noteNo){
        final Integer memberNo = loginMember.getMemberNo();
        List<NoteHistoryDto> noteHistoryDtoList = new ArrayList<>();

        // 채팅 내역 조회 대한 권한 검증
        Note note = findNoteById(noteNo);
        boolean isAuthority = note.getCreateMember().equals(memberNo) || note.getJoinMember().equals(memberNo);
        if(!isAuthority){
            throw new CustomException(ErrorCode.VALID_USER_ID);
        }

        // 쪽지 대화 내역 조회
        List<NoteHistory> historyList = note.getNoteHistoryList();

        for(NoteHistory history : historyList){

            // 요청한 회원이 보낸 쪽지는 true, 받은 쪽지는 false
            boolean isMine = memberNo.equals(history.getSenderNo());

            // 삭제 상태 추가
            LogicalDelEnum logicalDel;
            if(memberNo.equals(history.getSenderNo())){
                logicalDel = history.getSenderDel();
            } else {
                logicalDel = history.getReceiverDel();
            }

            NoteHistoryDto noteHistoryDto = NoteHistoryDto.builder()
                    .historyNo(history.getHistoryNo())
                    .mine(isMine)
                    .content(history.getContent())
                    .regDate(history.getRegDate())
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
    public void deleteHistory(SecurityUserDto loginMember, Integer historyNo){
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
