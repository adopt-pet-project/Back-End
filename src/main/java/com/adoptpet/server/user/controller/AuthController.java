package com.adoptpet.server.user.controller;

import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.user.domain.Token;
import com.adoptpet.server.user.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final TokenRepository tokenRepository;

    @GetMapping("/token/refresh")
    public ResponseEntity<String> refresh(@RequestBody Token tokens) {
        Optional<Token> token = tokenRepository.findAccessToken(tokens.getAccessToken());

        log.info("accessToken = {}", tokens.getAccessToken());
        log.info("accessTokenPresent = {}", token);

        log.info("true = {}", JwtUtil.verifyToken(token.get().getRefreshToken()));

        if (token.isPresent() && JwtUtil.verifyToken(token.get().getRefreshToken())) {
            String newAccessToken = JwtUtil.generateAccessToken(token.get().getEmail(), "USER");
            log.info("새 엑세스 토큰 = {}", newAccessToken);
            return ResponseEntity.ok().body(newAccessToken);
        }

        return ResponseEntity.internalServerError().build();
    }

}
