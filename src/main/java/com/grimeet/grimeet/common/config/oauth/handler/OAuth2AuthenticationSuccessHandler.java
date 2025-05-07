package com.grimeet.grimeet.common.config.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.common.constant.AuthConstants;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.util.jwt.JwtUtil;
import com.grimeet.grimeet.domain.auth.entity.RefreshToken;
import com.grimeet.grimeet.domain.auth.repository.RefreshTokenRepository;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.service.UserFacade;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  private final JwtUtil jwtUtil;
  private final UserFacade userFacade;
  private final RefreshTokenRepository refreshTokenRepository;
  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    log.info("✅ [OAuth2 Authentication Success Handler] Authentication successful: {}", authentication);

    // 1. OAuth2User에서 attribute 정보 추출
    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
    Map<String, Object> attributes = oAuth2User.getAttributes();

    // 2. Attribute에서 email 정보 추출
    String email = (String) attributes.get("email");

    UserResponseDto findUserDto;
    try {
      findUserDto = userFacade.findUserByEmail(email);
    } catch (GrimeetException e) {
      // 기존 회원이 아닌 경우 회원가입 처리
      log.info("🟠 [OAuth2 Authentication Success Handler] User not found. Need signup");

      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json;charset=UTF-8");

      Map<String, String> errorResponse = Map.of(
              "error", "signup_required",
              "message", "회원가입이 필요합니다."
      );

      response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
      return;
    }

    // 3. ✅ 기존 회원인 경우 JWT 토큰 생성
    User findUser = findUserDto.toEntity();
    UserPrincipalDetails userPrincipal = new UserPrincipalDetails(
            findUser,
            List.of(() -> "ROLE_USER")
    );

    String accessToken = jwtUtil.generateAccessToken(userPrincipal);
    String refreshToken = jwtUtil.generateRefreshToken(userPrincipal);

    // 4. RefreshToken 저장
    try {
      refreshTokenRepository.findByEmail(findUser.getEmail())
              .ifPresentOrElse(
                      existingToken -> {
                        existingToken.updateToken(refreshToken);
                        refreshTokenRepository.save(existingToken);
                      },
                      () -> {
                        refreshTokenRepository.save(new RefreshToken(findUser.getEmail(), refreshToken));
                      }
              );
    } catch (DataIntegrityViolationException e) {
      log.warn("⚠️ [OAuth2 Success Handler] RefreshToken 저장 중 중복 발생. 기존 토큰을 갱신 시도합니다.", e);

      // 이미 다른 스레드가 저장했을 가능성 있음 → 그냥 update 시도
      refreshTokenRepository.findByEmail(findUser.getEmail())
              .ifPresent(existingToken -> {
                existingToken.updateToken(refreshToken);
                refreshTokenRepository.save(existingToken);
              });
    }

    // 5. Response Header에 AccessToken 추가
    response.setHeader(AuthConstants.AUTHORIZATION_HEADER, AuthConstants.BEARER_PREFIX + accessToken);

    // 6. ResponseBody에 RefreshToken 추가
    Map<String, String> tokenResponse = Map.of(
            "refreshToken", refreshToken
    );

    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write(objectMapper.writeValueAsString(tokenResponse));
  }
}
