package com.grimeet.grimeet.domain.auth.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SendAuthCodeRequestDto {

    @Email
    @NotBlank
    private String email;
}
