package com.adoptpet.server.commons.security.filter;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.domain.Token;
import com.adoptpet.server.user.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/token/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String atc = request.getHeader("Authorization");


        if (!JwtUtil.verifyToken(atc)) {
            log.error("Access Token 만료!");
            throw new IllegalStateException("Access Token 만료!");
        }

        if (atc != null && JwtUtil.verifyToken(atc)) {
            String email = JwtUtil.getUid(atc);

            // DB연동을 안했으니 이메일 정보로 유저를 만들어주겠습니다
            SecurityUserDto userDto = SecurityUserDto.builder()
                    .email(email)
                    .name("이름이에용")
                    .picture("프로필 이미지에요").build();

            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }



    public Authentication getAuthentication(SecurityUserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    private String generateAccessToken(Optional<Token> atk, String email, String role) {
        if (atk.isEmpty()) {
            return null;
        } else {
            if (JwtUtil.verifyToken(atk.get().getRefreshToken())) {
                return null;
            } else {
                return null;
            }
        }
    }
}