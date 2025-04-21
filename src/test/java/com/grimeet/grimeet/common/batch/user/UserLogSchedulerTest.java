package com.grimeet.grimeet.common.batch.user;

import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.userLog.entity.UserLog;
import com.grimeet.grimeet.domain.userLog.repository.UserLogRepository;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.domain.user.dto.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
public class UserLogSchedulerTest {

    @Autowired
    private UserLogScheduler userLogScheduler;

    @Autowired
    private UserLogRepository userLogRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = userRepository.save(User.builder()
                .name("테스트유저")
                .email("test@example.com")
                .password("encoded")
                .nickname("테스트")
                .phoneNumber("010-1234-5678")
                .userStatus(UserStatus.NORMAL)
                .build());

        userLogRepository.save(UserLog.builder()
                .userId(user.getId())
                .lastLoginAt(LocalDate.now().minusDays(30))
                .changedPasswordAt(LocalDate.now().minusDays(90))
                .nextDormantCheckDate(LocalDate.now()) // 오늘 날짜!
                .nextNotificationDate(LocalDate.now().plusDays(10))
                .build());
    }
    @DisplayName("스케줄러가_휴면상태로_정상_전환한다")
    @Test
    void 스케줄러가_휴면상태로_정상_전환한다() {
        // when
        userLogScheduler.updateUserLogByDormantCheck();

        // then
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals(UserStatus.DORMANT, updatedUser.getUserStatus());
    }

    @DisplayName("nextDormantCheckDate가_미래인_유저는_변경되지_않는다")
    @Test
    void nextDormantCheckDate가_미래인_유저는_변경되지_않는다() {
        // given
        User futureUser = userRepository.save(User.builder()
                .name("미래유저")
                .email("future@example.com")
                .password("pw")
                .nickname("미래")
                .phoneNumber("010-9999-8888")
                .userStatus(UserStatus.NORMAL)
                .build());

        userLogRepository.save(UserLog.builder()
                .userId(futureUser.getId())
                .lastLoginAt(LocalDate.now())
                .changedPasswordAt(LocalDate.now())
                .nextDormantCheckDate(LocalDate.now().plusDays(5)) // 미래 날짜
                .nextNotificationDate(LocalDate.now().plusDays(30))
                .build());

        // when
        userLogScheduler.updateUserLogByDormantCheck();

        // then
        User updated = userRepository.findById(futureUser.getId()).orElseThrow();
        assertEquals(UserStatus.NORMAL, updated.getUserStatus());
    }

}