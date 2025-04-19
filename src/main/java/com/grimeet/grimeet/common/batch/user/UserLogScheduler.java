package com.grimeet.grimeet.common.batch.user;

import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.domain.user.service.UserService;
import com.grimeet.grimeet.domain.userLog.entity.UserLog;
import com.grimeet.grimeet.domain.userLog.repository.UserLogRepository;
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
    private final UserService userService;

    @Scheduled(cron = "0 0 0 * * *")
    public void updateUserLogByDormantCheck() {
        LocalDate now = LocalDate.now();
        List<UserLog> userLogs = userLogRepository.findByNextDormantCheckDateLessThanEqual(now);
        log.info("[스케줄러] 휴면 검사 대상 {}명 발견", userLogs.size());

        List<Long> userIds = userLogs.stream()
                .map(UserLog::getUserId)
                .toList();

        userService.updateUserStatusDormantBatch(userIds);

        log.info("[스케줄러] 휴면 전환 완료", userIds.size());
    }

}
