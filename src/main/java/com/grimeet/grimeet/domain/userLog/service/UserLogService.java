package com.grimeet.grimeet.domain.userLog.service;

import com.grimeet.grimeet.domain.userLog.dto.UserLogResponseDto;

import java.util.List;

public interface UserLogService {
  UserLogResponseDto createUserLog(Long userId);
  UserLogResponseDto updateUserLogByLogin(Long userId);
  boolean checkUserLogsForNotification(Long Id);
  boolean checkUserLogsForDormant(Long Id);
  UserLogResponseDto updateUserLogByPassword(Long userId);
  void convertUsersToDormant(List<Long> userIds);
  List<UserLogResponseDto> findAllUserLogsForDormantCheck();
}
