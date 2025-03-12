package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;

public interface AuthService {
  UserResponseDto register(UserCreateRequestDto userCreateRequestDto);

  AuthResponseDto createAccessToken(String refreshToken);

  String login(UserLoginRequestDto userLoginRequestDto);

  void logout(String userEmail);
}
