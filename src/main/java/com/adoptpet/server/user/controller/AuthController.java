package com.adoptpet.server.user.controller;

import com.adoptpet.server.commons.notification.repository.EmitterRepository;
import com.adoptpet.server.commons.security.dto.RefreshToken;
import com.adoptpet.server.commons.security.service.JwtUtil;
import com.adoptpet.server.commons.support.StatusResponseDto;
import com.adoptpet.server.commons.util.SecurityUtils;
import com.adoptpet.server.user.dto.response.TokenResponseStatus;
import com.adoptpet.server.user.repository.RefreshTokenRepository;
import com.adoptpet.server.user.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final RefreshTokenRepository tokenRepository;
    private final RefreshTokenService tokenService;
    private final JwtUtil jwtUtil;
    private final EmitterRepository emitterRepository;

    @PostMapping("token/logout")
    public ResponseEntity<StatusResponseDto> logout(@RequestHeader("Authorization") final String accessToken) {

        String id = String.valueOf(SecurityUtils.getUser().getMemberNo());

        // 엑세스 토큰으로 현재 Redis 정보 삭제
        tokenService.removeRefreshToken(accessToken);
        // 회원과 연결된 SseEmitter 객체를 제거
        emitterRepository.deleteAllStartWithId(id);
        // 알림 Event를 캐시에서 제거
        emitterRepository.deleteAllEventCacheStartWithId(id);

        return ResponseEntity.ok(StatusResponseDto.addStatus(200));
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenResponseStatus> refresh(@RequestHeader("Authorization") final String accessToken) {

        // 액세스 토큰으로 Refresh 토큰 객체를 조회
        Optional<RefreshToken> refreshToken = tokenRepository.findByAccessToken(accessToken);

        // RefreshToken이 존재하고 유효하다면 실행
        if (refreshToken.isPresent() && jwtUtil.verifyToken(refreshToken.get().getRefreshToken())) {
            // RefreshToken 객체를 꺼내온다.
            RefreshToken resultToken = refreshToken.get();
            // 권한과 아이디를 추출해 새로운 액세스토큰을 만든다.
            String newAccessToken = jwtUtil.generateAccessToken(resultToken.getId(), jwtUtil.getRole(resultToken.getRefreshToken()));
            // 액세스 토큰의 값을 수정해준다.
            resultToken.updateAccessToken(newAccessToken);
            tokenRepository.save(resultToken);
            // 새로운 액세스 토큰을 반환해준다.
            return ResponseEntity.ok(TokenResponseStatus.addStatus(200, accessToken));
        }

        return ResponseEntity.badRequest().body(TokenResponseStatus.addStatus(400, null));
    }

}
