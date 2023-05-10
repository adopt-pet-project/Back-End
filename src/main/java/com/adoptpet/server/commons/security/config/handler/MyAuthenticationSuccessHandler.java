package com.adoptpet.server.commons.security.config.handler;

import com.adoptpet.server.commons.security.dto.Token;
import com.adoptpet.server.commons.security.dto.SecurityUserDto;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.repository.TokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenRepository tokenRepository;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String provider = oAuth2User.getAttribute("provider");

        boolean isExist = oAuth2User.getAttribute("exist");

        if (!isExist) {
            log.info("토큰 발행 시작");
            Token token = JwtUtil.generateToken(email, "USER");
            log.info("token = {}", token);


            // DB연동을 안했으니 이메일 정보로 유저를 만들어주겠습니다
            SecurityUserDto userDto = SecurityUserDto.builder()
                    .email(email)
                    .name("이름이에용")
                    .picture("프로필 이미지에요").build();


            tokenRepository.save(com.adoptpet.server.user.domain.Token.builder()
                    .accessToken(token.getToken())
                    .refreshToken(token.getRefreshToken())
                    .email(email)
                    .build()
            );



            response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:3000/loginCheck")
                    .queryParam("access_token", token.getToken())
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString());


        } else {
            response.sendRedirect(UriComponentsBuilder.fromUriString("http://localhost:3000/register")
                    .queryParam("email", (String) oAuth2User.getAttribute("email"))
                    .queryParam("provider", provider)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString()
            );
        }
    }

    public Authentication getAuthentication(SecurityUserDto member) {
        return new UsernamePasswordAuthenticationToken(member, "",
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
