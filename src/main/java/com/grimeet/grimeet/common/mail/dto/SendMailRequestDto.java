package com.grimeet.grimeet.common.mail.dto;

import lombok.Getter;

@Getter
public class SendMailRequestDto {
    private String to;
    private String subject;
    private String body;
}
