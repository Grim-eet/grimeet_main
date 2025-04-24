package com.grimeet.grimeet.common.mail.service;

public interface EmailVerificationService {
    void sendVerificationCode(String email);
    boolean verifyCode(String email, String code);
}
