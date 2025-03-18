package com.grimeet.grimeet.domain.userLog.dto;

import com.grimeet.grimeet.domain.userLog.entity.UserLog;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "사용자 로그 응답")
@Getter
@AllArgsConstructor
public class UserLogResponseDto {
  @Schema(description = "마지막 로그인 일시", example = "2024-10-01 00:00:00")
  private String lastLoginAt;

  @Schema(description = "최근 비밀번호 변경 일시", example = "2024-10-01 00:00:00")
  private String changedPasswordAt;

  @Schema(description = "휴면 상태 전환 예정일", example = "2023-10-01 00:00:00")
  private String nextDormantCheckDate;

  @Schema(description = "다음 비밀번호 변경 알림 예정일", example = "2024-11-01 00:00:00")
  private String nextNotificationDate;

  @Schema(description = "사용자 ID", example = "1")
  private Long userId;

  @Builder
  public UserLogResponseDto(UserLog userLog) {
    this.lastLoginAt = userLog.getLastLoginAt().toString();
    this.changedPasswordAt = userLog.getChangedPasswordAt().toString();
    this.nextDormantCheckDate = userLog.getNextDormantCheckDate().toString();
    this.nextNotificationDate = userLog.getNextNotificationDate().toString();
    this.userId = userLog.getUserId();
  }
}
