package com.grimeet.grimeet.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 토큰 갱신 응답 DTO
 *
 * @Author : Jgone2
 * @Since : 2025-04-21
 */
@Schema(description = "토큰 갱신 응답")
@Getter
@Builder
@AllArgsConstructor
public class TokenRefreshResponseDto {
  @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb3BhbFByaW5jZXNzOTlAZ21haWwuY29tIiwiaWF0IjoxNjM0NzQwNjYwLCJleHAiOjE2MzQ3NDQyNjB9.7")
  private String accessToken;
  @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb3BhbFByaW5jZXNzOTlAZ21haWwuY29tIiwiaWF0IjoxNjM0NzQwNjYwLCJleHAiOjE2MzQ3NDQyNjB917")
  private String refreshToken;
}
