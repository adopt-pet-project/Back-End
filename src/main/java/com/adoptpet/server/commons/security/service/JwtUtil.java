package com.adoptpet.server.commons.security.service;

import com.adoptpet.server.commons.security.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;


@Slf4j
public abstract class JwtUtil {
    private static String secretKey = "token-secret-key";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }


    public static Token generateToken(String uid, String role) {
        long tokenPeriod = 1000L * 30L;
        long refreshPeriod = 1000L * 60L * 60L * 24L * 30L * 3L;

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        Date now = new Date();
        return new Token(
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact(),
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + refreshPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact()
        );
    }

    public static String generateAccessToken(String uid, String role) {
        long tokenPeriod = 1000L * 30L;

        Claims claims = Jwts.claims().setSubject(uid);
        claims.put("role", role);

        Date now = new Date();
        return
                Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(now)
                        .setExpiration(new Date(now.getTime() + tokenPeriod))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();

    }


    public static boolean verifyToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            log.error("토큰 파싱 중 에러 발생!");
            log.error("에러 발생 토큰 = {}", token);
            return false;
        }
    }

    public static String getEmail(String token) {
        return String.valueOf(
                Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject()
        );
    }


    public static String getUid(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}