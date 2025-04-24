package com.grimeet.grimeet.common.cache.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void setAuthCode(String email, String code, long ttlSeconds) {
        redisTemplate.opsForValue().set(email, code, Duration.ofSeconds(ttlSeconds));
        log.info("[CacheService] Redis에 인증코드 저장: email={}, code={}, TTL={}초", email, code, ttlSeconds);
    }

    @Override
    public String getAuthCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }
}
