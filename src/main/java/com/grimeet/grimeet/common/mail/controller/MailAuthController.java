package com.grimeet.grimeet.common.mail.controller;

import com.grimeet.grimeet.common.mail.dto.SendAuthCodeRequestDto;
import com.grimeet.grimeet.common.mail.dto.VerifyAuthCodeRequestDto;
import com.grimeet.grimeet.common.mail.service.EmailVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "mail_auth", description = "이메일 인증과 관련된 API")
public class MailAuthController {

    private final EmailVerificationService authVerificationService;

    @Operation(summary = "이메일 인증 코드 전송", description = "입력한 이메일 주소로 인증 코드를 전송합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 코드 전송 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 이메일 형식")
    })
    @PostMapping("/email-code")
    public ResponseEntity<String> sendVerificationCode(@Valid @RequestBody SendAuthCodeRequestDto requestDto) {
        authVerificationService.sendVerificationCode(requestDto.getEmail());
        return ResponseEntity.ok().body("이메일을 발송하였습니다.");
    }

    @Operation(summary = "이메일 인증 코드 확인", description = "입력한 인증 코드가 유효한지 검증합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "인증 성공"),
            @ApiResponse(responseCode = "400", description = "인증 실패")
    })
    @PostMapping("/email-code/verify")
    public ResponseEntity<String> verifyAuthCode(@Valid @RequestBody VerifyAuthCodeRequestDto requestDto) {
        boolean isVerified = authVerificationService.verifyCode(requestDto.getEmail(), requestDto.getCode());
        return isVerified ?
                ResponseEntity.ok().body("이메일 인증에 성공했습니다.") :
                ResponseEntity.badRequest().body("이메일 인증에 실패했습니다.");
    }

}
