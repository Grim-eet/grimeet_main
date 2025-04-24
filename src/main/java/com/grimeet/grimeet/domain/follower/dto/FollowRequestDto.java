package com.grimeet.grimeet.domain.follower.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowRequestDto {
  @Schema(description = "팔로우할 대상 유저 ID", example = "2")
  private Long followingId;
}
