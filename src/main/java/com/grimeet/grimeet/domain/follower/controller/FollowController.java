package com.grimeet.grimeet.domain.follower.controller;

import com.grimeet.grimeet.domain.follower.dto.FollowRequestDto;
import com.grimeet.grimeet.domain.follower.dto.UserSummaryResponseDto;
import com.grimeet.grimeet.domain.follower.service.FollowService;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@Tag(name = "Follow", description = "팔로우 API")
public class FollowController {

  private final FollowService followService;

  @Operation(summary = "팔로우 토글", description = "현재 로그인된 사용자가 특정 유저를 팔로우 또는 언팔로우합니다.")
  @PostMapping()
  public ResponseEntity<Void> toggleFollow(
          @AuthenticationPrincipal UserPrincipalDetails userDetails,
          @RequestBody FollowRequestDto request
  ) {
    Long followerId = userDetails.getUser().getId();
    followService.follow(followerId, request.getFollowingId());
    return ResponseEntity.ok().build();
  }
  @Operation(summary = "팔로워 목록 조회", description = "특정 유저를 팔로우한 사용자 목록을 조회합니다.")
  @GetMapping("/followers/{userId}")
  public ResponseEntity<List<UserSummaryResponseDto>> getFollowers(@PathVariable Long userId) {
    List<UserSummaryResponseDto> followers = followService.getFollowers(userId).stream()
            .map(UserSummaryResponseDto::from)
            .collect(Collectors.toList());
    return ResponseEntity.ok(followers);
  }

  @Operation(summary = "팔로잉 목록 조회", description = "특정 유저가 팔로우한 사용자 목록을 조회합니다.")
  @GetMapping("/followings/{userId}")
  public ResponseEntity<List<UserSummaryResponseDto>> getFollowings(@PathVariable Long userId) {
    List<UserSummaryResponseDto> followings = followService.getFollowings(userId).stream()
            .map(UserSummaryResponseDto::from)
            .collect(Collectors.toList());
    return ResponseEntity.ok(followings);
  }

  @Operation(summary = "팔로우 여부 확인", description = "현재 로그인한 사용자가 특정 유저를 팔로우 중인지 확인합니다.")
  @GetMapping("/is-following")
  public ResponseEntity<Boolean> isFollowing(
          @AuthenticationPrincipal UserPrincipalDetails userDetails,
          @RequestParam Long followingId
  ) {
    Long followerId = userDetails.getUser().getId();
    boolean result = followService.isFollowing(followerId, followingId);
    return ResponseEntity.ok(result);
  }
}
