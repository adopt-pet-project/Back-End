package com.adoptpet.server.commons.security.filter;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().equals("/token/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        String atc = request.getHeader("Authorization");


        if (!jwtUtil.verifyToken(atc)) {
            log.error("Access Token 만료!");
            throw new IllegalStateException("Access Token 만료!");
        }

        if (atc != null && jwtUtil.verifyToken(atc)) {
            String email = jwtUtil.getUid(atc);
            String role = jwtUtil.getRole(atc);

            // DB연동을 안했으니 이메일 정보로 유저를 만들어주겠습니다
            SecurityUserDto userDto = SecurityUserDto.builder()
                    .email(email)
                    .role(role)
                    .name("이름이에용")
                    .picture("프로필 이미지에요").build();

            log.info("ROLE - ATC = {}", role);

            Authentication auth = getAuthentication(userDto);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }



    public Authentication getAuthentication(SecurityUserDto member) {
        log.info("ROLE = {}", member.getRole());
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority(member.getRole())));
    }

}