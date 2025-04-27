package com.grimeet.grimeet.domain.socialAccount.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
@Schema(description = "소셜 계정 요청 DTO", title = "SocialAccountRequestDto")
public class SocialAccountRequestDto {

  @Schema(description = "소셜 계정 고유 ID", example = "1234567890")
  @NotNull(message = "소셜 아이디는 필수입니다.")
  private String socialId;

  @Schema(description = "소셜 계정 제공자", example = "NAVER")
  @NotNull(message = "소셜 계정 제공자는 필수입니다.")
  private Provider provider;

}
