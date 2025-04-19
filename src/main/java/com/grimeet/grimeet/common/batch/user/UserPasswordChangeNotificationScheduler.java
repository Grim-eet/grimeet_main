package com.grimeet.grimeet.common.batch.user;

import com.grimeet.grimeet.domain.userLog.dto.UserLogResponseDto;
import com.grimeet.grimeet.domain.userLog.service.UserLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserPasswordChangeNotificationScheduler {

  private final UserLogService userLogService;

  @Scheduled(cron = "0 0 3 * * *") // 매일 3시
  public void notifyPasswordChange() {
    log.info("📢 비밀번호 변경 알림 스케줄 시작");

    List<UserLogResponseDto> targets = userLogService.findAllUserLogsForNotification();

    targets.forEach(user -> {
      // 알림 전송 로직
//      notificationService.sendPasswordChangeNotice(user.getUserId());
    });

    // 추후 알림 관련 회의 이후 구현 예정

    log.info("✅ 총 {}명의 사용자에게 알림 전송 완료", targets.size());
  }
}
