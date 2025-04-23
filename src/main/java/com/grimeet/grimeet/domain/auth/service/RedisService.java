package com.grimeet.grimeet.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    // Redis에 값 저장, 유효시간 설정
    public void setCode(String key, String value, long expireTime) {
        redisTemplate.opsForValue().set(key, value, expireTime, TimeUnit.MINUTES);
    }

    // Redis에서 값 조회
    public String getCode(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    //Redis에서 값 삭제
    public void deleteCode(String key) {
        redisTemplate.delete(key);
    }
}
