package com.grimeet.grimeet.common.cache.service;

public interface CacheService {

    void setAuthCode(String email, String code, long ttlSecondes);

    String getAuthCode(String email);
}
