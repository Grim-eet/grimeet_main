package com.grimeet.grimeet.domain.social_account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Schema(description = "소셜 계정 응답 DTO", title = "SocialAccountResponseDto")
public class SocialAccountResponseDto {

  @Schema(description = "소셜 계정 고유 ID", example = "1234567890")
  private String socialId;
  @Schema(description = "소셜 계정 제공자", example = "NAVER")
  private String provider;

  @Builder
  public SocialAccountResponseDto(String socialId, String provider) {
    this.socialId = socialId;
    this.provider = provider;
  }

  public static SocialAccountResponseDto of(String socialId, String provider) {
    return new SocialAccountResponseDto(socialId, provider);
  }
}
