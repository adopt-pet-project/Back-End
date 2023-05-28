package com.adoptpet.server.commons.notification.controller;

import com.adoptpet.server.commons.notification.dto.NotificationsResponse;
import com.adoptpet.server.commons.notification.service.NotificationService;
import com.adoptpet.server.commons.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notification")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;




    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> test(
//            @RequestHeader(name = "Authorization") String accessToken,
            @RequestParam("num") String num) {
        return ResponseEntity.ok().body(notificationService.test(num));
    }


    /**
    * @title 로그인 한 유저 SSE 연결
    **/
//    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter connect(
//            @RequestHeader(name = "Authorization") String accessToken,
//            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
//        return notificationService.subscribe(accessToken, lastEventId);
//    }

    /**
     * @title 로그인 한 유저의 모든 알림 조회
     */
    @GetMapping("/all")
    public ResponseEntity<NotificationsResponse> notifications() {
        return ResponseEntity.ok().body(notificationService.findAllById(SecurityUtils.getUser()));
    }

    /**
     * @title 알림 읽음 상태 변경
     */
    @PatchMapping("/read")
    public ResponseEntity<Void> readNotification(@PathVariable Long id) {
        notificationService.readNotification(id);
        return ResponseEntity.noContent().build();
    }

}