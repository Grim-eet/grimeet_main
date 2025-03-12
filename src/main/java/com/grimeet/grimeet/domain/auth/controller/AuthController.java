package com.grimeet.grimeet.domain.auth.controller;

import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.auth.service.AuthService;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
    UserResponseDto registeredUser = authService.register(userCreateRequestDto);
    User findUser = registeredUser.toEntity(registeredUser);
    UserLoginRequestDto registerUserLoginInfo = new UserLoginRequestDto(findUser.getEmail(), findUser.getPassword());
    String accessToken = authService.login(registerUserLoginInfo);
    return ResponseEntity.status(HttpStatus.CREATED).body(accessToken); // 또는 성공 메시지만 반환
  }

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
    String accessToken = authService.login(userLoginRequestDto);
    return ResponseEntity.ok(new AuthResponseDto(accessToken));
  }

//  @PostMapping("/logout")
//  public ResponseEntity<String> logout(@AuthenticationPrincipal String userEmail) {
//    authService.logout(refreshToken, userEmail);
//    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("로그아웃 되었습니다.");
//  }

}
