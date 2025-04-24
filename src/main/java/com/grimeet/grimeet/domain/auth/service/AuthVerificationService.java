package com.grimeet.grimeet.domain.auth.service;

public interface AuthVerificationService {
    void sendVerificationCode(String email);
    boolean verifyCode(String email, String code);
}
