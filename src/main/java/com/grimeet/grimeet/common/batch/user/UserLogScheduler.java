package com.grimeet.grimeet.common.batch.user;

import com.grimeet.grimeet.domain.user.dto.UserStatus;
import com.grimeet.grimeet.domain.user_log.entity.UserLog;
import com.grimeet.grimeet.domain.user_log.repository.UserLogRepository;
import com.grimeet.grimeet.domain.user_log.service.UserLogFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserLogScheduler {

    private final UserLogRepository userLogRepository;
    private final UserLogFacade userLogFacade;

    @Scheduled(cron = "0 0 3 * * *")    // 매일 오전 3시
    public void updateUserLogByDormantCheck() {
        try {
            LocalDate now = LocalDate.now();
            List<UserLog> userLogs = userLogRepository.findCandidatesForDormantCheck(now, List.of(UserStatus.NORMAL, UserStatus.SOCIAL));
            log.info("[UserLogScheduler] 휴면 검사 대상 {}명 발견", userLogs.size());

            if (userLogs.isEmpty()) return;

            List<Long> userIds = userLogs.stream()
                    .map(UserLog::getUserId)
                    .toList();

            userLogFacade.convertUsersToDormant(userIds); // 💡 이 부분만 바꾼 것

            log.info("[UserLogScheduler] 휴면 전환 완료: 총 {}명 처리", userIds.size());
        } catch (Exception e) {
            log.error("[UserLogScheduler] 예외 발생: {}", e.getMessage(), e);
        }
    }

}
