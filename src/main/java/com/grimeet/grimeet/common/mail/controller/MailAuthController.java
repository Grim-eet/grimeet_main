package com.grimeet.grimeet.common.mail.controller;

import com.grimeet.grimeet.common.mail.dto.SendAuthCodeRequestDto;
import com.grimeet.grimeet.common.mail.dto.VerifyAuthCodeRequestDto;
import com.grimeet.grimeet.common.mail.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MailAuthController {

    private final EmailVerificationService authVerificationService;

    @PostMapping("/email-code")
    public ResponseEntity<String> sendVerificationCode(@Valid @RequestBody SendAuthCodeRequestDto requestDto) {
        authVerificationService.sendVerificationCode(requestDto.getEmail());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email-code/verify")
    public ResponseEntity<String> verifyAuthCode(@Valid @RequestBody VerifyAuthCodeRequestDto requestDto) {
        boolean isVerified = authVerificationService.verifyCode(requestDto.getEmail(), requestDto.getCode());
        return isVerified ?
                ResponseEntity.ok().build() :
                ResponseEntity.badRequest().build();
    }

}
