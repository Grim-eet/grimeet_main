package com.grimeet.grimeet.common.mail.controller;

import com.grimeet.grimeet.common.mail.dto.SendMailRequestDto;
import com.grimeet.grimeet.common.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMail(@RequestBody SendMailRequestDto requestDto) {
        mailService.sendEmail(requestDto.getTo(), requestDto.getSubject(), requestDto.getBody());
        return ResponseEntity.ok("메일 전송 완료");
    }
}
