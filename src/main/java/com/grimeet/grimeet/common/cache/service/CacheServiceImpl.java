package com.grimeet.grimeet.common.cache.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService {

    @Override
    public void setAuthCode(String email, String code, long ttlSecondes) {

    }

    @Override
    public String getAuthCode(String email) {
        return "";
    }
}
