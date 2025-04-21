package com.grimeet.grimeet.common.batch.user;

import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.userLog.entity.UserLog;
import com.grimeet.grimeet.domain.userLog.repository.UserLogRepository;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.common.batch.user.UserLogScheduler;
import com.grimeet.grimeet.domain.user.dto.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserLogSchedulerTest {

    @Autowired
    private UserLogScheduler scheduler;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("스케줄러유저")
                .email("scheduler@example.com")
                .password("encoded")
                .nickname("스케줄러")
                .phoneNumber("010-0000-0000")
                .userStatus(UserStatus.NORMAL)
                .build());

        // 휴면 검사 날짜가 오늘인 경우만 대상이 됨
        userLogRepository.save(UserLog.builder()
                .userId(user.getId())
                .nextDormantCheckDate(LocalDate.now()) // 오늘!
                .build());
    }

    @Test
    void 휴면_대상_유저를_정상적으로_처리한다() {
        scheduler.updateUserLogByDormantCheck();

        User updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getUserStatus()).isEqualTo(UserStatus.DORMANT);
    }

}

