package com.adoptpet.server.commons.config.interceptor;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE + 99)
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())) {
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            if (!jwtUtil.verifyToken(accessToken)) {
                throw new IllegalStateException("토큰이 만료되었습니다.");
            } else {
//                accessor.setUser(getAuthentication(accessToken));
            }
        }

        return message;
    }

    private Authentication getAuthentication(String accessToken) {
        // AccessToken 내부의 payload에 있는 email로 user를 조회한다. 없다면 예외를 발생시킨다 -> 정상 케이스가 아님
        Member findMember = memberRepository.findByEmail(jwtUtil.getUid(accessToken))
                .orElseThrow(IllegalStateException::new);

        // SecurityContext에 등록할 User 객체를 만들어준다.
        SecurityUserDto userDto = SecurityUserDto.builder()
                .memberNo(findMember.getMemberNo())
                .email(findMember.getEmail())
                .role("ROLE_".concat(findMember.getUserRole()))
                .nickname(findMember.getNickname())
                .build();

        return new UsernamePasswordAuthenticationToken(userDto, "",
                List.of(new SimpleGrantedAuthority(userDto.getRole())));
    }
}
