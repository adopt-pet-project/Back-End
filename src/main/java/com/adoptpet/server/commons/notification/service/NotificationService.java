package com.adoptpet.server.commons.notification.service;

import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.notification.domain.Notification;
import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.dto.NotificationResponse;
import com.adoptpet.server.commons.notification.repository.EmitterRepository;
import com.adoptpet.server.commons.notification.repository.NotificationRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 1740000L;
    public static final String PREFIX_URL = "https://pet-hub.site/";
    private final EmitterRepository emitterRepository;
    private final MemberService memberService;
    private final NotificationRepository notificationRepository;

    //== SSE 연결 ==//
    @Transactional
    public SseEmitter subscribe(SecurityUserDto loginMember, String lastEventId) {

        Integer memberNo = loginMember.getMemberNo();
        // Emitter map에 저장하기 위한 key 생성
        String id = memberNo + "_" + System.currentTimeMillis();
        // SseEmitter map에 저장
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        log.info("new emitter added: {}", emitter);

        // emitter의 완료 또는 타임아웃 Event가 발생할 경우, 해당 emitter를 삭제
        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        // 503 에러를 방지하기 위한 더미 Event 전송
        sendToClient(emitter, id, "EventStream Created. [userId=" + memberNo + "]");

        // 클라이언트가 미수신한 Event 목록이 존재할 경우 전송하여 Event 유실을 예방
        if (!lastEventId.isEmpty()) {

            // id에 해당하는 eventCache 찾음
            Map<String, Object> events = emitterRepository.findAllEventCacheStartWithId(String.valueOf(memberNo));

            // 미수신한 Event 목록 전송
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(emitter, entry.getKey(), entry.getValue()));
        }

        return emitter;
    }

    @Transactional
    public void send(Member sender, Member receiver, NotifiTypeEnum type, Integer refNo , String content) {
        send(sender,receiver,type,String.valueOf(refNo),content);
    }



    //== 알림 전송 ==//
    @Transactional
    public void send(Member sender, Member receiver, NotifiTypeEnum type, String resource , String content) {
        // 알림 생성
        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .type(type)
                .Url(PREFIX_URL + type.getPath() + resource)
                .content(content)
                .isRead(false)
                .isDel(false)
                .build();

        // SseEmitter 캐시 조회를 위해 key의 prefix 생성
        String id = String.valueOf(notification.getReceiver().getMemberNo());
        // 알림 저장
        notificationRepository.save(notification);
        // 로그인 한 유저의 SseEmitter 모두 가져오기
        Map<String, SseEmitter> sseEmitters = emitterRepository.findAllStartWithById(id);
        sseEmitters.forEach(
                (key, emitter) -> {
                    // 데이터 캐시 저장(유실된 데이터 처리하기 위함)
                    emitterRepository.saveEventCache(key, notification);
                    // 데이터 전송
                    sendToClient(emitter, key, NotificationResponse.from(notification));
                }
        );
    }

    //== 클라이언트에 SSE + 알림 데이터 전송 ==//
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
        } catch (IOException ex) {
            emitterRepository.deleteById(id);
            log.error("--- SSE 연결 오류 ----", ex.getMessage());
        }
    }

    //== 알림 생성 ==//
    private Notification createNotification(Member sender, Member receiver, NotifiTypeEnum type, Integer refNo, String content) {
        return Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .type(type)
                .Url(PREFIX_URL + type.getPath() + refNo)
                .content(content)
                .isRead(false)
                .isDel(false)
                .build();
    }


    //== 로그인 맴버 알림 전체 조회 ==//
    @Transactional(readOnly = true)
    public List<NotificationResponse> findAllById(SecurityUserDto loginMember) {

        Member member = memberService.findByMemberNo(loginMember.getMemberNo());
        // 채팅과 쪽지의 마지막 알림 조회
        List<Notification> chatOrNote
                = notificationRepository.findChatOrNoteByReceiver(member.getMemberNo());
        // 채팅과 쪽지 이외의 알림 전체 조회
        List<Notification> other
                = notificationRepository.findOtherByReceiver(member.getMemberNo());
        // 위에서 조회한 두 알림 리스트 합침
        chatOrNote.addAll(other);

        return chatOrNote.stream()
                .map(NotificationResponse::from)
                .sorted(Comparator.comparing(NotificationResponse::getId).reversed())
                .collect(Collectors.toList());
    }



    //== 알림 읽음 처리 ==//
    @Transactional
    public void readNotification(Long id) {
        Notification notification = getNotification(id);
        notification.read();
    }


    //== 알림 삭제 ==//
    @Transactional
    public void deleteNotification(Long[] idList) {
        for(Long id : idList){
             Notification notification = getNotification(id);
             notificationRepository.delete(notification);
        }
    }


    //== 개별 알림 조회 ==//
    private Notification getNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(ErrorCode::throwNotificationNotFound);
    }
}
