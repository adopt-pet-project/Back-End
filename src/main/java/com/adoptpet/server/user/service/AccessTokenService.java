package com.adoptpet.server.user.service;

import com.adoptpet.server.commons.security.dto.RefreshToken;
import com.adoptpet.server.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final RefreshTokenRepository tokenRepository;

    @Transactional
    public void saveTokenInfo(String email, String refreshToken, String accessToken) {
        tokenRepository.save(new RefreshToken(email, accessToken, refreshToken));
    }
}
