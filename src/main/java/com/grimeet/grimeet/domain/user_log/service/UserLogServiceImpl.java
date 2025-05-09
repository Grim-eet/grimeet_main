package com.grimeet.grimeet.domain.user_log.service;

import com.grimeet.grimeet.domain.user_log.dto.UserLogResponseDto;
import com.grimeet.grimeet.domain.user_log.entity.UserLog;
import com.grimeet.grimeet.domain.user_log.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLogServiceImpl implements UserLogService {

  private final UserLogRepository userLogRepository;

  /**
   * 사용자 로그 생성(회원가입 시 자동 생성)
   * @param userId
   * @return UserLogResponseDto
   */
  @Override
  public UserLogResponseDto createUserLog(Long userId) {
    LocalDate now = LocalDate.now();
    UserLog userLog = UserLog.builder()
            .userId(userId)
            .lastLoginAt(now)
            .changedPasswordAt(now)
            .nextDormantCheckDate(now.plusDays(365))
            .nextNotificationDate(now.plusDays(90))
            .build();
    return new UserLogResponseDto(userLogRepository.save(userLog));
  }
  /**
   * 로그인 시 사용자 로그 업데이트
   * @param userId
   * @return UserLogResponseDto
   */
  @Override
  public UserLogResponseDto updateUserLogByLogin(Long userId) {
    UserLog userLog = userLogRepository.findUserLogByUserId(userId);
    LocalDate now = LocalDate.now();
    userLog.updateLastLoginAt(now);
    userLog.updateNextDormantCheckDate(now.plusDays(365));
    return new UserLogResponseDto(userLogRepository.save(userLog));
  }

  /**
   * 비밀번호 변경 시 사용자 로그 업데이트
   * @param userId
   * @return UserLogResponseDto
   */
  @Override
  public UserLogResponseDto updateUserLogByPassword(Long userId) {
    UserLog userLog = userLogRepository.findUserLogByUserId(userId);
    LocalDate now = LocalDate.now();
    userLog.updateChangedPasswordAt(now);
    userLog.updateNextNotificationDate(now.plusDays(90));
    return new UserLogResponseDto(userLogRepository.save(userLog));
  }

  /**
   * 휴면 사용자 조회
   * @return List<UserLogResponseDto>
   */
  @Override
  public List<UserLogResponseDto> findAllUserLogsForDormantCheck() {
    LocalDate now = LocalDate.now();
    List<UserLog> userLogs = userLogRepository.findByNextDormantCheckDateLessThanEqual(now);

    if (userLogs.isEmpty()) {
      log.info("[휴면 사용자 조회] 대상 없음 (기준일자: {})", now);
      return List.of();
    }

    log.info("[휴면 사용자 조회] 기준일자: {}, 대상자 수: {}", now, userLogs.size());
    return userLogs.stream().map(UserLogResponseDto::new).toList();
  }

  /**
   * 다음 비밀번호 변경 알림 예정일이 지났는지 확인
   * @return boolean
   */
  @Override
  public boolean checkUserLogsForNotification(Long userId) {
    return userLogRepository.findNextNotificationDateAfter(userId, LocalDate.now()) != null;
  }

  @Override
  public boolean checkUserLogsForDormant(Long Id) {
    UserLog userLog = userLogRepository.findUserLogByUserId(Id);
    if (userLog == null) {
      return false;
    }
    LocalDate now = LocalDate.now();
    LocalDate nextDormantCheckDate = userLog.getNextDormantCheckDate();
    if (nextDormantCheckDate != null && nextDormantCheckDate.isBefore(now)) {
      return true;  // 휴면 전환 가능
    }
    log.info("[UserLogService] 휴면 전환 불가: 다음 체크일자: {}, 현재일자: {}", nextDormantCheckDate, now);
    return false;
  }

  /**
   * 휴면 사용자로의 전환
   * @param userIds
   */
  @Override
  public void convertUsersToDormant(List<Long> userIds) {
    log.info("[UserLogService] 휴면 전환된 사용자 ID: {}", userIds);
  }
}
