package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.cache.CacheService;
import com.grimeet.grimeet.common.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthVerificationServiceImpl implements AuthVerificationService {

    private final MailService mailService;
    private final CacheService cacheService;

    private static final long TTL_SECONDS = 300; // 5분

    @Override
    public void sendVerificationCode(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        cacheService.setCode(email, code, TTL_SECONDS);
        mailService.sendEmail(email, "[Grimeet] 인증 코드", "인증 코드: " + code); // 이메일 내용, 수정확인
    }

    @Override
    public boolean verifyCode(String email, String code) {
        String cacheCode = cacheService.getCode(email);
        return code.equals(cacheCode);
    }
}
