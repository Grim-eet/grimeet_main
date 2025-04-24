package com.grimeet.grimeet.common.mail.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class SendMailRequestDto {

    @Email
    @NotBlank
    private String to;

    private String subject;

    private String body;
}
