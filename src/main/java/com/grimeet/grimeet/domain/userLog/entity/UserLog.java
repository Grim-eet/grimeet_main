package com.grimeet.grimeet.domain.userLog.entity;

import com.grimeet.grimeet.common.entity.BaseTime;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER_LOGS")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = true)
public class UserLog extends BaseTime {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "user_log_id")
  private Long id;

  @Schema(description = "마지막 로그인 일시", example = "2024-10-01 00:00:00")
  @Comment("마지막 로그인 일시")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "last_login_at", nullable = false, updatable = true, columnDefinition = "DATETIME")
  private Date lastLoginAt;

  @Schema(description = "최근 비밀번호 변경 일시", example = "2024-10-01 00:00:00")
  @Comment("최근 비밀번호 변경 일시")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "changed_password_at", nullable = false, updatable = true, columnDefinition = "DATETIME")
  private Date changedPasswordAt;

  @Schema(description = "휴면 상태 전환 예정일", example = "2023-10-01 00:00:00")
  @Comment("휴면 상태 전환 예정일")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "next_dormant_check_date", nullable = false, updatable = true, columnDefinition = "DATETIME")
  private Date nextDormantCheckDate;

  @Schema(description = "다음 비밀번호 변경 알림 예정일", example = "2024-11-01 00:00:00")
  @Comment("다음 비밀번호 변경 알림 예정일")
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "next_notification_date", nullable = false, updatable = true, columnDefinition = "DATETIME")
  private Date nextNotificationDate;

  @Schema(description = "사용자 ID", example = "1")
  @Comment("사용자 ID")
  @Column(name = "user_id", nullable = false, updatable = false)
  private Long userId;

  public void updateLastLoginAt(Date lastLoginAt) {
    this.lastLoginAt = lastLoginAt;
  }

  public void updateChangedPasswordAt(Date changedPasswordAt) {
    this.changedPasswordAt = changedPasswordAt;
  }

  public void updateNextDormantCheckDate(Date nextDormantCheckDate) {
    this.nextDormantCheckDate = nextDormantCheckDate;
  }

  public void updateNextNotificationDate(Date nextNotificationDate) {
    this.nextNotificationDate = nextNotificationDate;
  }
}
