package com.grimeet.grimeet.common.email.service;

public interface EmailService {

    void sendEmail(String to, String subject, String body);
}
