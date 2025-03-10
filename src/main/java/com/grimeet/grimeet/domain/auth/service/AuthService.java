package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;

public interface AuthService {
  String createAccessToken(String refreshToken);

  String login(UserLoginRequestDto userLoginRequestDto);

  void logout(String refreshToken);
}
