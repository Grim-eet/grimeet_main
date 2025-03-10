package com.grimeet.grimeet.domain.auth.dto;

import lombok.Getter;

@Getter
public class UserLoginRequestDto {

  private String email;

  private String password;
}
