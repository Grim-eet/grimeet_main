package com.grimeet.grimeet.common.config.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.common.jwt.JwtUtil;
import com.grimeet.grimeet.domain.auth.entity.RefreshToken;
import com.grimeet.grimeet.domain.auth.repository.RefreshTokenRepository;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;
  private final UserService userService;
  private final RefreshTokenRepository refreshTokenRepository;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private final String AUTH_HEADER = "Authorization";
  private final String TOKEN_PREFIX = "Bearer ";

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    log.info("✅ [OAuth2 Authentication Success Handler] Authentication successful: {}", authentication);

    // 1. 인증된 사용자 정보 가져오기
    UserPrincipalDetails principal = (UserPrincipalDetails) authentication.getPrincipal();
    String userEmail = principal.getUsername();
    log.info("💡 [OAuth2 Authentication Success Handler] Sign-in User email: {}", userEmail);

    // 2. 기존 회원 여부 확인
    UserResponseDto findUser = userService.findUserByEmail(userEmail);
    log.info("💡 [OAuth2 Authentication Success Handler] User found: {}", findUser);

    // 3. Access Token과 Refresh Token 생성
    String accessToken = jwtUtil.generateAccessToken(principal);
    String refreshToken = jwtUtil.generateRefreshToken(principal);

    // 4. Refresh Token 저장
    refreshTokenRepository.findByEmail(findUser.getEmail())
            .ifPresentOrElse(
                    existingToken -> {
                      existingToken.updateToken(refreshToken);  // 기존 Refresh Token이 있으면 업데이트
                      refreshTokenRepository.save(existingToken);
                    },
                    () -> {
                      // 기존이 없으면 새로 생성
                      refreshTokenRepository.save(new RefreshToken(findUser.getEmail(), refreshToken));
                    }
            );

    // 5. Access Token을 Response Header에 추가
    response.setHeader(AUTH_HEADER, TOKEN_PREFIX + accessToken);

    response.setStatus(HttpServletResponse.SC_OK);

    Map<String, String> tokens = Map.of("refreshToken", refreshToken);

    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(tokens));
  }
}
