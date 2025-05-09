package com.grimeet.grimeet.common.oauth;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class OAuthStateJwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private static final long EXPIRATION_MS = 5 * 60 * 1000;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createStateToken(String provider) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject("oauth_state")
                .claim("provider", provider)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims validateStateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.info("[JWT 디버그] state 수신값 = {}", token);
            log.info("[JWT 디버그] 현재 서명 키 해시 = {}", key.hashCode());
            throw new GrimeetException(ExceptionStatus.OAUTH2_INVALID_STATE);
        }
    }
}
