package com.grimeet.grimeet.domain.userLog.scheduler;

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

    @Scheduled(cron = "0 0 0 * * *")
    public void updateUserLogByDormantCheck() {
        LocalDate now = LocalDate.now();
        List<UserLog> userLogs = userLogRepository.findByNextDormantCheckDateLessThanEqual(now);

        if (userLogs.isEmpty()) {
            log.info("[스케줄러] 휴면 검사 대상 없음 (기준일: {})", now);
            return;
        }

        log.info("[스케줄러] 휴면 검사 대상 {}명 발견", userLogs.size());

        for (UserLog userLog : userLogs) {
            userLog.updateNextDormantCheckDate(userLog.getNextDormantCheckDate().plusDays(365));
            userLogRepository.save(userLog);
        }

        userLogRepository.saveAll(userLogs);
        log.info("[스케줄러] 휴면 검사 주기 갱신 완료");
    }

}
