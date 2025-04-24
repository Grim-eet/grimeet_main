package com.grimeet.grimeet.common.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void setAuthCode(String email, String code, long ttlSecondes) {
        redisTemplate.opsForValue().set(email, code, ttlSecondes);
    }

    @Override
    public String getAuthCode(String email) {
        return redisTemplate.opsForValue().get(email);
    }
}
