package com.grimeet.grimeet.domain.userLog.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * UserLogService의 메소드를 호출하여 UserLog를 생성하는 Facade 클래스
 * @author jgone2
 */
@Component
@RequiredArgsConstructor
public class UserLogFacade {

  private final UserLogService userLogService;

  /**
   * 유저의 비밀번호 변경 로그를 업데이트하는 메소드
   * @param userId Long
   */
  public void updatePasswordLog(Long userId) {
    userLogService.updateUserLogByPassword(userId);
  }

  /**
   * 유저 로그인 시 로그를 업데이트하는 메소드
   * @param userId Long
   */
  public void updateLoginLog(Long userId) {
    userLogService.updateUserLogByLogin(userId);
  }

  /**
   * 유저의 비밀번호 변경 권장 여부를 체크하는 메소드
   * @param userId Long
   */
  public boolean checkNotificationRequired(Long userId) {
    return userLogService.checkUserLogsForNotification(userId);
  }

  /**
   * 유저의 휴면회원 전환 여부를 체크하는 메소드
   * @param userId Long
   */
  public boolean checkDormantUserLog(Long userId) {
    return userLogService.checkUserLogsForDormant(userId);
  }

  /**
   * 유저의 로그를 생성하는 메소드
   * @param userId Long
   */
  public void createUserLog(Long userId) {
    userLogService.createUserLog(userId);
  }

  /**
   * 유저의 휴면회원으로의 전환을 처리하는 메소드
   * @param userIds List<Long>
   */
  public void convertUsersToDormant(List<Long> userIds) {
    userLogService.convertUsersToDormant(userIds);
  }
}

