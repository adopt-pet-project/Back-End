package com.adoptpet.server.commons.config.interceptor;

import com.adoptpet.server.adopt.service.ChatRoomService;
import com.adoptpet.server.adopt.service.ChatService;
import com.adoptpet.server.commons.security.service.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("presend 검증 = " + accessor.getCommand());
        // StompCommand에 따라서 로직을 분기해서 처리하는 메서드를 호출한다.
        String email = verifyAccessToken(getAccessToken(accessor));
        handleMessage(accessor.getCommand(), accessor, email);
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String email) {
        switch (stompCommand) {

            case CONNECT:
                connectToChatRoom(accessor, email);
                break;
            case SUBSCRIBE:
            case SEND:
                verifyAccessToken(getAccessToken(accessor));
                break;
        }
    }

    private String getAccessToken(StompHeaderAccessor accessor) {
        return accessor.getFirstNativeHeader("Authorization");
    }

    private void connectToChatRoom(StompHeaderAccessor accessor, String email) {
        // 채팅방 번호를 가져온다.
        Integer chatRoomNo = getChatRoomNo(accessor);

        // 채팅방 입장 처리 -> Redis에 입장 내역 저장
        chatRoomService.connectChatRoom(chatRoomNo, email);
        // 읽지 않은 채팅을 전부 읽음 처리
        chatService.updateCountAllZero(chatRoomNo, email);
        // 현재 채팅방에 접속중인 인원이 있는지 확인한다.
        boolean isConnected = chatRoomService.isConnected(chatRoomNo);

        if (isConnected) {
            chatService.updateMessage(email, chatRoomNo);
        }
    }

    private String verifyAccessToken(String accessToken) {
        if (!jwtUtil.verifyToken(accessToken)) {
            throw new IllegalStateException("토큰이 만료되었습니다.");
        }

        return jwtUtil.getUid(accessToken);
    }

    private Integer getChatRoomNo(StompHeaderAccessor accessor) {
        return
                Integer.valueOf(
                        Objects.requireNonNull(
                                accessor.getFirstNativeHeader("chatRoomNo")
                        ));
    }
}
