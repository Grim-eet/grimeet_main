package com.grimeet.grimeet.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 로그인 요청 응답 DTO
 *
 * @author : Jgone2
 * @since : 2025-04-21
 */
@Schema(description = "로그인 요청 응답 - Service layer")
@Builder
@Getter
@AllArgsConstructor
public class AuthResponseDto {
  @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb3BhbFByaW5jZXNzOTlAZ21haWwuY29tIiwiaWF0IjoxNjM0NzQwNjYwLCJleHAiOjE2MzQ3NDQyNjB9.7")
  private String accessToken;
  @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb3BhbFByaW5jZXNzOTlAZ21haWwuY29tIiwiaWF0IjoxNjM0NzQwNjYwLCJleHAiOjE2MzQ3NDQyNjB917")
  private String refreshToken;
  @Schema(description = "비밀번호 변경 필요 여부, true: 필요, false: 불필요", example = "false")
  private Boolean isPasswordChangeRequired;
  @Schema(description = "휴면계정여부, true: 휴면, false: 비휴면", example = "false")
  private Boolean isDormant;
}
