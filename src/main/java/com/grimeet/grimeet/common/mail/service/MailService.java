package com.grimeet.grimeet.common.mail.service;

public interface MailService {

    void sendEmail(String to, String subject, String body);
}
