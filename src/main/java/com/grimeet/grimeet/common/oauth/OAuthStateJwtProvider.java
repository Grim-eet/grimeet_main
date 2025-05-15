package com.grimeet.grimeet.common.oauth;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class OAuthStateJwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${crypto.aes.secret}")
    private String aesSecret;

    private static final long EXPIRATION_MS = 5 * 60 * 1000;

    private Key jwtKey;
    private SecretKeySpec aesKeySpec;

    @PostConstruct
    public void init() {
        this.jwtKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // AES 암호화 키
        byte[] keyBytes = aesSecret.getBytes(StandardCharsets.UTF_8);
        if (!(keyBytes.length == 16 || keyBytes.length == 24 || keyBytes.length == 32)) {
            throw new GrimeetException(ExceptionStatus.INVALID_AES_KEY_LENGTH);
        }
        this.aesKeySpec = new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 암호화된 username과 provider를 담은 state JWT 생성
     * @param username
     * @param provider
     * @return jwt
     */
    public String createStateToken(String username, String provider) {
        long now = System.currentTimeMillis();
        String encryptedUsername = encrypt(username);

        return Jwts.builder()
                .setSubject("oauth_state")
                .claim("username", encryptedUsername)
                .claim("provider", provider)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + EXPIRATION_MS))
                .signWith(jwtKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * state JWT 검증 (유효성 + 서명 확인)
     * @param token
     * @return
     */
    public Claims validateStateToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            log.warn("[ERROR] OAuth state 토큰 검증 실패. token = {}", token);
            throw new GrimeetException(ExceptionStatus.OAUTH2_INVALID_STATE);
        }
    }

    /**
     * 암호화된 username을 복호화
     * @param claims
     * @return
     */
    public String extractDecryptedUsername(Claims claims) {
        String encryptedUsername = claims.get("username", String.class);
        return decrypt(encryptedUsername);
    }

    /**
     * AES 암호화
     * @param plainText
     * @return
     */
    private String encrypt(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aesKeySpec);
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            log.error("[ERROR] AES 암호화 실패", e);
            throw new GrimeetException(ExceptionStatus.INVALID_AES_KEY_LENGTH);
        }
    }

    /**
     * AES 복호화
     * @param encryptedText
     * @return
     */
    private String decrypt(String encryptedText) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, aesKeySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("[ERROR] AES 복호화 실패", e);
            throw new GrimeetException(ExceptionStatus.INVALID_AES_KEY_LENGTH);
        }
    }
}
