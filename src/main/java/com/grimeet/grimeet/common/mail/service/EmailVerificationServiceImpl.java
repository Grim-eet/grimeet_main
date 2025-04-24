package com.grimeet.grimeet.common.mail.service;

import com.grimeet.grimeet.common.cache.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final MailService mailService;
    private final CacheService cacheService;

    @Value("${spring.mail.verification.auth-code-ttl}")
    private long authCodeTtl; // 15분

    @Override
    public void sendVerificationCode(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        cacheService.setAuthCode(email, code, authCodeTtl);
        mailService.sendEmail(email, "[Grimeet] 인증 코드", "인증 코드: " + code); // 이메일 내용, 수정확인
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String cachedCode = cacheService.getAuthCode(email);
        boolean matched = code.equals(cachedCode);
        if (matched) {
            log.info("[AuthVerificationService] 인증 성공: email={}, code={}", email, code);
        } else {
            log.warn("[AuthVerificationService] 인증 실패: email={}, inputCode={}, savedCode={}", email, code, cachedCode);
        }
        return matched;
    }
}
