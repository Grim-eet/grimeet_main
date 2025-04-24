package com.grimeet.grimeet.common.mail.service;

import com.grimeet.grimeet.common.cache.service.CacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * 이메일(사용자 계정), 인증코드를 mailService에게 전달한다.
 * Redis에 저장된 인증 코드를 CacheService를 통해 갖고 와, 사용자가 제출한 코드와 일치하는지 검증한다.
 * @author mirim
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final MailService mailService;
    private final CacheService cacheService;

    /**
     * 인증 코드가 만료되는 기간
     * 15분으로 설정함
     */
    @Value("${spring.mail.verification.auth-code-ttl}")
    private long authCodeTtl;

    /**
     * 숫자, 영어 대문자 조합으로 6자리 랜덤하게 만들어 redis에 저장하고 사용자가 입력한 이메일로 인증 코드 메일을 보낸다.
     * @param email
     */
    @Override
    public void sendVerificationCode(String email) {
        String code = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        cacheService.setAuthCode(email, code, authCodeTtl);
        mailService.sendEmail(email, "[Grimeet] 인증 코드", "인증 코드: " + code); // 이메일 내용, 수정확인
    }

    /**
     * 사용자가 제출한 코드가 redis에 저장된 인증코드와 일치하는지 boolean 타입으로 return 한다.
     * 이때 만료 시간이 지나면 인증코드(value)의 값은 null이 된다.
     * @param email
     * @param code
     * @return 인증 성공 ? true : false
     */
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
