package com.adoptpet.server.commons.config.interceptor;

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

@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선 순위를 높게 설정해서 SecurityFilter들 보다 앞서 실행되게 해준다.
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        // StompHeaderAccessor를 이용하여 메시지를 래핑한다.
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        // 연결을 시도하거나, 메시지를 전송하는 경우에 jwt 토큰을 검증한다.
        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            // 클라이언트에서 보내온 액세스토큰을 얻어온다.
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            // 액세스토큰이 만료된경우 예외를 발생시킨다.
            if (!jwtUtil.verifyToken(accessToken)) {
                throw new IllegalStateException("토큰이 만료되었습니다.");
            }
        }

        return message;
    }
}
