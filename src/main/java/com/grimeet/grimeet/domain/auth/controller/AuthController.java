package com.grimeet.grimeet.domain.auth.controller;

import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.auth.service.AuthService;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "회원가입, 로그인, 로그아웃 API")
public class AuthController {

  private final AuthService authService;
  @Value("${cookie.max-age.access}")
  private int cookieMaxAge;

  @Operation(summary = "회원가입", description = "회원가입을 진행합니다.")
  @ApiResponses({
          @ApiResponse(responseCode = "201", description = "회원가입 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @PostMapping("/register")
  public ResponseEntity<String> register(@Valid @RequestBody UserCreateRequestDto userCreateRequestDto) {
    authService.register(userCreateRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED).body("회원가입이 완료되었습니다.");
  }

  @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인을 진행합니다.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "로그인 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @PostMapping("/login")
  public ResponseEntity<Void> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
    String accessToken = authService.login(userLoginRequestDto);

    // 쿠키 생성 및 설정
    Cookie cookie = new Cookie("Authorization_Access", accessToken);
    cookie.setHttpOnly(true);
    cookie.setSecure(false);  // 배포 환경 시 true로 변경
    cookie.setPath("/");  // 모든 경로에서 접근 가능하도록 설정
    cookie.setMaxAge(cookieMaxAge);  // 24시간 동안 유효
    response.addCookie(cookie);

    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "로그아웃", description = "로그아웃을 진행합니다.")
  @ApiResponses({
          @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
          @ApiResponse(responseCode = "400", description = "잘못된 요청")
  })
  @PostMapping("/logout")
  public void logout() {}
//  public ResponseEntity<String> logout(@AuthenticationPrincipal(expression = "username") String userEmail) {
//    authService.logout(userEmail);
//    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("로그아웃 되었습니다.");
//  }
}
