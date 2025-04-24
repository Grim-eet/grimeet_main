package com.grimeet.grimeet.domain.auth.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class VerifyAuthCodeRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String code;

}
