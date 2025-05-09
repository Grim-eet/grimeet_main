package com.grimeet.grimeet.domain.user_log.service;

import com.grimeet.grimeet.domain.user_log.dto.UserLogResponseDto;

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
