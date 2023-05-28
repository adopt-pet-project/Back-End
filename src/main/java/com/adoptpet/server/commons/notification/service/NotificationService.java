package com.adoptpet.server.commons.notification.service;

import com.adoptpet.server.commons.exception.CustomException;
import com.adoptpet.server.commons.exception.ErrorCode;
import com.adoptpet.server.commons.notification.domain.Notification;
import com.adoptpet.server.commons.notification.domain.NotifiTypeEnum;
import com.adoptpet.server.commons.notification.dto.NotificationResponse;
import com.adoptpet.server.commons.notification.dto.NotificationsResponse;
import com.adoptpet.server.commons.notification.repository.EmitterRepository;
import com.adoptpet.server.commons.notification.repository.NotificationRepository;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    public static final String PREFIX_URL = "http://localhost/";
    private final EmitterRepository emitterRepository;
    private final MemberService memberService;
    private final NotificationRepository notificationRepository;
    private final JwtUtil jwtUtil;

    @Transactional
    public SseEmitter test(String num){

        String id = num + "_" + System.currentTimeMillis();
        SseEmitter emitter = emitterRepository.save(id, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(id));
        emitter.onTimeout(() -> emitterRepository.deleteById(id));

        sendToClient(emitter, id, "EventStream Created. [userId=" + num + "]");

        return emitter;
    }



    @Transactional
    public SseEmitter subscribe(SecurityUserDto loginMember, String lastEventId) {

        Integer memberNo = loginMember.getMemberNo();
        // Emitter 캐시에 저장하기 위한 key 생성
        String id = memberNo + "_" + System.currentTimeMillis();
        // SseEmitter 캐시에 저장
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
    public void send(Member member, NotifiTypeEnum type, Integer refNo , String content) {
        // 알림 생성
        Notification notification = createNotification(member, type,refNo, content);
        // SseEmitter 캐시 조회를 위해 key의 prefix 생성
        String id = String.valueOf(member.getMemberNo());
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


    //== 클라이언트에 SSE 응답 ==//
    private void sendToClient(SseEmitter emitter, String id, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .id(id)
                    .name("sse")
                    .data(data));
            log.info("::::::::::: SSE :::::::::::::: {} ", data);

        } catch (IOException ex) {
            emitterRepository.deleteById(id);
            log.error("--- SSE 연결 오류 ----", ex);
            throw new CustomException(ErrorCode.TEST);
        }
    }

    private Notification createNotification(Member member, NotifiTypeEnum type,Integer refNo, String content) {
        return Notification.builder()
                .member(member)
                .type(type)
                .Url(PREFIX_URL+type.getPath() + refNo)
                .content(content)
                .isRead(false)
                .isDel(false)
                .build();
    }


    /**
    * @title 로그인 맴버 알림 전체 조회
    **/
    @Transactional
    public NotificationsResponse findAllById(SecurityUserDto loginMember) {

        Member member = memberService.findByMemberNo(loginMember.getMemberNo());

        // 회원 엔티티로 알림 조회 후 알림 response List로 변환
        List<NotificationResponse> responses = notificationRepository.findAllByMember(member).stream()
                .map(NotificationResponse::from)
                .collect(Collectors.toList());

        // 안읽은 알림 개수 체크
        long unreadCount = responses.stream()
                .filter(notification -> !notification.isRead())
                .count();

        return NotificationsResponse.of(responses, unreadCount);
    }


    @Transactional
    public void readNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 알림입니다."));
        notification.read();
    }

}
