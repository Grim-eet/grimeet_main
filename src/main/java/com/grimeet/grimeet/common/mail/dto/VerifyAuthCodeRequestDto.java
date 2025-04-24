package com.grimeet.grimeet.common.mail.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyAuthCodeRequestDto {

    @Schema(description = "사용자의 이메일 주소", example = "test@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "이메일 인증용 인증코드", example = "TEST12")
    private String code;

}
