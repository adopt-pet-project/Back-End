package com.adoptpet.server.user.tokenservice;

import com.adoptpet.server.commons.security.dto.RefreshToken;
import com.adoptpet.server.user.repository.RefreshTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class RedisTokenServiceTest {

    @Autowired
    private RefreshTokenRepository repository;

    @BeforeEach
    public void after() {
        repository.deleteAll();
    }

    @Test
    public void 기본_등록_조회() {
        // given
        String email = "ckdekrn88@gmail.com";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        RefreshToken resultToken = new RefreshToken(email, accessToken ,refreshToken);


        // when
        repository.save(resultToken);

        // then
        RefreshToken findToken = repository.findByAccessToken(resultToken.getAccessToken()).get();
        Assertions.assertThat(findToken.getAccessToken()).isEqualTo(accessToken);
        Assertions.assertThat(findToken.getRefreshToken()).isEqualTo(refreshToken);
        Assertions.assertThat(findToken.getId()).isEqualTo(email);
        log.info("token = {}", findToken);

    }

    @Test
    public void 수정_조회() {
        // given
        String email = "ckdekrn88@gmail.com";
        String accessToken = "accessToken";
        String refreshToken = "refreshToken";
        RefreshToken resultToken = new RefreshToken(email, accessToken ,refreshToken);


        // when
        repository.save(resultToken);
        RefreshToken findToken = repository.findByAccessToken(resultToken.getAccessToken()).get();
        findToken.updateAccessToken("액세스토큰");
        repository.save(findToken);
        RefreshToken updatedToken = repository.findByAccessToken("액세스토큰").get();

        // then
        Assertions.assertThat(updatedToken.getAccessToken()).isEqualTo("액세스토큰");
        Assertions.assertThat(updatedToken.getId()).isEqualTo("ckdekrn88@gmail.com");

    }
}
