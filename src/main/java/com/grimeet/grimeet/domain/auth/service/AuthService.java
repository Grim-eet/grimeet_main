package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;

public interface AuthService {
  void register(UserCreateRequestDto userCreateRequestDto);

  AuthResponseDto createAccessToken(String refreshToken);

  String login(UserLoginRequestDto userLoginRequestDto);

  void logout(String refreshToken);
}
