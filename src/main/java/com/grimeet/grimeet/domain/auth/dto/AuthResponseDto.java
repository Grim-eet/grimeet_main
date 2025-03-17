package com.grimeet.grimeet.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "로그인 요청 응답")
@Getter
@AllArgsConstructor
public class AuthResponseDto {
  @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb3BhbFByaW5jZXNzOTlAZ21haWwuY29tIiwiaWF0IjoxNjM0NzQwNjYwLCJleHAiOjE2MzQ3NDQyNjB9.7")
  private String AccessToken;
}
