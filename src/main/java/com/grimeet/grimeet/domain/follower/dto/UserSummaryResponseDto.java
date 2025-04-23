package com.grimeet.grimeet.domain.follower.dto;

import com.grimeet.grimeet.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "간단한 유저 정보 응답 DTO")
@Getter
@Builder
@AllArgsConstructor
public class UserSummaryResponseDto {

  @Schema(description = "유저 ID", example = "1")
  private Long id;
  @Schema(description = "유저 닉네임", example = "jaegon98")
  private String nickname;
  @Schema(description = "프로필 이미지 URL", example = "https://s3.aws.com/image/profile.jpg")
  private String profileImageUrl;

  public static UserSummaryResponseDto from(User user) {
    return new UserSummaryResponseDto(
            user.getId(),
            user.getNickname(),
            user.getProfileImageUrl()
    );
  }
}
