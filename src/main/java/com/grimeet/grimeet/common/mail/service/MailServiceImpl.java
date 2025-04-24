package com.grimeet.grimeet.common.mail.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 메일을 송신하는 기능
 * @author mirim
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    /**
     * 받는 이, 제목, 내용으로 이메일을 보낸다.
     * 관련되누 smpt 설정은 application.yml에 위치한다.
     * @param to
     * @param subject
     * @param body
     */
    @Override
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
        log.info("[MailService] 인증용 메일 전송 완료");
    }
}
