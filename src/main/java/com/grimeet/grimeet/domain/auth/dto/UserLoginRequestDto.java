package com.grimeet.grimeet.domain.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginRequestDto {

  private String email;

  private String password;
}
