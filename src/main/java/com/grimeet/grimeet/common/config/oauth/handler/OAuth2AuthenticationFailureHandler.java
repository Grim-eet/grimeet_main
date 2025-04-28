package com.grimeet.grimeet.common.config.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    log.error("❌ [OAuth2 Authentication Failure Handler] Authentication failed: {}", exception.getMessage());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 에러 반환
    response.setContentType("application/json;charset=UTF-8");

    // 에러 메시지 구성
    Map<String, String> errorResponse = new HashMap<>();
    String errorCode = "OAUTH2_AUTHENTICATION_FAILED";

    if (exception.getMessage().contains("redirect_uri")) {
      errorCode = "INVALID_REDIRECT_URI";
    }
    if (exception.getMessage().contains("access_denied")) {
      errorCode = "USER_DENIED_CONSENT";
    }

    errorResponse.put("error", errorCode);
    errorResponse.put("status", String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
    errorResponse.put("message", exception.getMessage());

    // JSON으로 응답
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }
}
