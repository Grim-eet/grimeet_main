package com.grimeet.grimeet.domain.userLog.service;

import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.service.UserService;
import com.grimeet.grimeet.domain.userLog.dto.UserLogResponseDto;
import com.grimeet.grimeet.domain.userLog.entity.UserLog;
import com.grimeet.grimeet.domain.userLog.repository.UserLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLogServiceImpl implements UserLogService {

  private final UserLogRepository userLogRepository;
  private final UserService userService;

  /**
   * 사용자 로그 생성(회원가입 시 자동 생성)
   * @param userEmail
   * @return UserLogResponseDto
   */
  @Override
  public UserLogResponseDto createUserLog(String userEmail) {
    UserResponseDto findUserResponseDto = userService.findUserByEmail(userEmail);
    LocalDate now = LocalDate.now();
    UserLog userLog = UserLog.builder()
            .lastLoginAt(now)
            .changedPasswordAt(now)
            .nextDormantCheckDate(updateNextDormantCheckDate(now))
            .nextNotificationDate(updateNextNotificationDate(now))
            .userId(findUserResponseDto.getId())
            .build();
    UserLog savedUserLog = userLogRepository.save(userLog);
    return new UserLogResponseDto(savedUserLog);
  }

  @Override
  public UserLogResponseDto findUserLogByUserId(Long userId) {
    return null;
  }

  @Override
  public UserLogResponseDto findUserLogById(Long id) {
    return null;
  }

  /**
   * 로그인 시 사용자 로그 업데이트
   * @param userEmail
   * @return
   */
  @Override
  public UserLogResponseDto updateUserLogByLogin(String userEmail) {
    UserResponseDto findUserResponseDto = userService.findUserByEmail(userEmail);
    UserLog userLog = userLogRepository.findUserLogByUserId(findUserResponseDto.getId());

    LocalDate now = LocalDate.now();
    userLog.updateLastLoginAt(now);
    userLog.updateNextDormantCheckDate(updateNextDormantCheckDate(now));
    UserLog updatedUserLog = userLogRepository.save(userLog);
    return new UserLogResponseDto(updatedUserLog);
  }

  @Override
  public UserLogResponseDto updateUserLogByPassword(String userEmail) {
    UserResponseDto findUserResponseDto = userService.findUserByEmail(userEmail);
    UserLog userLog = userLogRepository.findUserLogByUserId(findUserResponseDto.getId());

    LocalDate now = LocalDate.now();
    userLog.updateChangedPasswordAt(now);
    userLog.updateNextNotificationDate(updateNextNotificationDate(now));
    UserLog updatedUserLog = userLogRepository.save(userLog);
    return new UserLogResponseDto(updatedUserLog);
  }

  @Override
  public List<UserLogResponseDto> findAllUserLogsForDormantCheck() {

    return List.of();
  }

  @Override
  public List<UserLogResponseDto> findAllUserLogsForNotification() {
    return List.of();
  }

  /**
   * 일자 추가
   * @param date
   * @param days
   * @return Date
   */
  private LocalDate addDays(LocalDate date, int days) {
    // LocalDate에 일자 추가
    LocalDate result = date.plusDays(days);
    // LocalDate -> Date
    return result;
  }

  /**
   * 다음 휴면 상태 전환 예정일 업데이트
   * @param date
   * @return Date
   */
  private LocalDate updateNextDormantCheckDate(LocalDate date) {
    return addDays(date, 365);
  }

  /**
   * 다음 비밀번호 변경 알림 예정일 업데이트
   * @param date
   * @return Date
   */
  private LocalDate updateNextNotificationDate(LocalDate date) {
    return addDays(date, 90);
  }
}
