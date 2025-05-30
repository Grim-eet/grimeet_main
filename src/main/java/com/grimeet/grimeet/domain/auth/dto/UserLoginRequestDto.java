package com.grimeet.grimeet.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginRequestDto {

  @Schema(description = "이메일(로그인 아이디)", example = "dopalPrincess98@gmail.com")
  private String email;

  @Schema(description = "비밀번호", example = "test1234!#")
  private String password;
}
