package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.TokenRefreshResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;

public interface AuthService {
  UserResponseDto register(UserCreateRequestDto userCreateRequestDto);

  TokenRefreshResponseDto createAccessToken(String refreshToken);

  AuthResponseDto login(UserLoginRequestDto userLoginRequestDto);

//  void logout(String userEmail);
}
