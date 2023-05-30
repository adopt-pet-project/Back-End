package com.adoptpet.server.commons.notification.controller;

import com.adoptpet.server.commons.notification.dto.DeleteNotificationsRequest;
import com.adoptpet.server.commons.notification.dto.NotificationResponse;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/notification")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    /**
     * @title 로그인 한 유저 SSE 연결
     **/
    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId,
            HttpServletResponse response) {

        SseEmitter sseEmitter = notificationService.subscribe(SecurityUtils.getUser(), lastEventId);

        response.setHeader("X-Accel-Buffering","no");

        return ResponseEntity.ok(sseEmitter);
    }

    /**
     * @title 로그인 한 유저의 모든 알림 조회
     */
//    @GetMapping("/all")
//    public ResponseEntity<List<NotificationResponse>> notifications() {
//        return ResponseEntity.ok(notificationService.findAllById(SecurityUtils.getUser()));
//    }

    /**
     * @title 알림 읽음 상태 변경
     */
    @PatchMapping("/checked/{id}")
    public ResponseEntity<StatusResponseDto> readNotification(@PathVariable("id") Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.ok(StatusResponseDto.success());
    }

    /**
    * @title 알림 삭제
    **/
    @PostMapping
    public ResponseEntity<StatusResponseDto> deleteNotification(
            @RequestBody DeleteNotificationsRequest request){
        notificationService.deleteNotification(request.getIdList());
        return ResponseEntity.ok(StatusResponseDto.success());
    }
}