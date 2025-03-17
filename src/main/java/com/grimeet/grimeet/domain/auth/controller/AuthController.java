package com.grimeet.grimeet.domain.auth.controller;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.auth.service.AuthService;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
  @Value("${cookie.max-age.access}")
  private int cookieMaxAge;

  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
    authService.register(userCreateRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
  }

  @PostMapping("/login")
  public ResponseEntity<void> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
    String accessToken = authService.login(userLoginRequestDto);

    // 쿠키 생성 및 설정
    Cookie cookie = new Cookie("Authorization_Access", accessToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);  // 배포 환경 시 true로 변경
    cookie.setPath("/");  // 모든 경로에서 접근 가능하도록 설정
    cookie.setMaxAge(cookieMaxAge);  // 24시간 동안 유효

    return ResponseEntity.status(HttpStatus.OK).build();
  }

//  @PostMapping("/logout")
//  public ResponseEntity<String> logout(@AuthenticationPrincipal(expression = "username") String userEmail) {
//    authService.logout(userEmail);
//    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("로그아웃 되었습니다.");
//  }
}
