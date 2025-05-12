package com.grimeet.grimeet.domain.auth.controller;

import com.grimeet.grimeet.common.constant.AuthConstants;
import com.grimeet.grimeet.domain.auth.dto.AuthLoginResponseDto;
import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.TokenRefreshResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.auth.service.AuthService;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Auth", description = "회원가입, 로그인, 로그아웃 API")
public class AuthController {

  private final AuthService authService;


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
  public ResponseEntity<AuthLoginResponseDto> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto, HttpServletResponse response) {
    AuthResponseDto tokenDto = authService.login(userLoginRequestDto);

    return ResponseEntity.ok()
            .header(AuthConstants.AUTHORIZATION_HEADER, AuthConstants.BEARER_PREFIX + tokenDto.getAccessToken())
            .body(AuthLoginResponseDto.builder()
                    .refreshToken(tokenDto.getRefreshToken())
                    .isPasswordChangeRequired(tokenDto.getIsPasswordChangeRequired())
                    .isDormant(tokenDto.getIsDormant())
                    .build());
  }

  @Operation(summary = "로그아웃", description = "AccessToken 쿠키를 삭제하고 RefreshToken을 무효화합니다.")
  @ApiResponses({
          @ApiResponse(responseCode = "204", description = "로그아웃 성공"),
          @ApiResponse(responseCode = "401", description = "인증 실패"),
          @ApiResponse(responseCode = "500", description = "서버 내부 에러")
  })
  @PostMapping("/logout")
  public void logout() {}
//  public ResponseEntity<String> logout(@AuthenticationPrincipal(expression = "username") String userEmail) {
//    authService.logout(userEmail);
//    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("로그아웃 되었습니다.");
//  }

  @Operation(summary = "AccessToken 재발급", description = "RefreshToken을 이용하여 AccessToken을 재발급합니다.")
  @ApiResponses({
          @ApiResponse(responseCode = "200", description = "재발급 성공"),
          @ApiResponse(responseCode = "401", description = "유효하지 않은 토큰")
  })
  @PostMapping("/refresh")
  public ResponseEntity<TokenRefreshResponseDto> refreshAccessToken(
          @RequestHeader("Authorization") String authorizationHeader
  ) {
    String refreshToken = extractToken(authorizationHeader); // "Bearer " 제거
    TokenRefreshResponseDto response = authService.createAccessToken(refreshToken);
    return ResponseEntity.ok(response);
  }

  private String extractToken(String tokenHeader) {
    if (tokenHeader != null && tokenHeader.startsWith("Bearer ")) {
      return tokenHeader.substring("Bearer ".length());
    }
    throw new IllegalArgumentException("잘못된 Authorization 헤더 형식입니다.");
  }
}
