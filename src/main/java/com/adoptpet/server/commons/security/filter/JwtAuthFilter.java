package com.adoptpet.server.commons.security.filter;

import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.domain.Member;
import com.adoptpet.server.user.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("어디서 온 요청 ? = {}", request.getRequestURI());

        if (request.getRequestURI().contains("/token/")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("토큰 발급시에도 하니?");

        String atc = request.getHeader("Authorization");


        if (!jwtUtil.verifyToken(atc)) {
            log.error("Access Token 만료!");
            throw new IllegalStateException("Access Token 만료!");
        }

        if (atc != null && jwtUtil.verifyToken(atc)) {

            Member findMember = memberRepository.findByEmail(jwtUtil.getUid(atc))
                    .orElseThrow(IllegalStateException::new);

            SecurityUserDto userDto = SecurityUserDto.builder()
                    .memberNo(findMember.getMemberNo())
                    .email(findMember.getEmail())
                    .role("ROLE_".concat(findMember.getUserRole()))
                    .name("이름이에용")
                    .picture("프로필 이미지에요").build();


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