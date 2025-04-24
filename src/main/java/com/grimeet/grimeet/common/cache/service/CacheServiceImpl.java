package com.grimeet.grimeet.common.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Redis에 이메일 인증의 인증 코드를 set하고 저장한 인증코드를 get합니다.
 * @author mirim
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final StringRedisTemplate redisTemplate;

    /**
     * redis에 이메일(사용자 계정), 인증코드, 만료시간을 저장합니다.
     * @param email
     * @param code
     * @param ttlSeconds
     */
    @Override
    public void setAuthCode(String email, String code, long ttlSeconds) {
        redisTemplate.opsForValue().set(email, code, Duration.ofSeconds(ttlSeconds));
        log.info("[CacheService] Redis에 인증코드 저장: email={}, code={}, TTL={}초", email, code, ttlSeconds);
    }

    /**
     * redis에 key: 이메일(사용자 계정)인 value: 인증코드를 조회합니다.
     * @param email
     * @return value 값으로 저장된 인증 코드(시간 만료시 null)
     */
    @Override
    public String getAuthCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }
}
