package com.grimeet.grimeet.domain.userLog.service;

import com.grimeet.grimeet.domain.userLog.dto.UserLogResponseDto;

import java.util.List;

public interface UserLogService {
  UserLogResponseDto createUserLog(String userEmail);
  UserLogResponseDto findUserLogByUserId(Long userId);
  UserLogResponseDto findUserLogById(Long id);
  UserLogResponseDto updateUserLogByLogin(String userEmail);
  boolean checkUserLogsForNotification(Long Id);
  UserLogResponseDto updateUserLogByPassword(String userEmail);
  List<UserLogResponseDto> findAllUserLogsForDormantCheck();
  List<UserLogResponseDto> findAllUserLogsForNotification();
}
