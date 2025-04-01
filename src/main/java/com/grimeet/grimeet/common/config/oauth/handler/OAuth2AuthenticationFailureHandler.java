package com.grimeet.grimeet.common.config.oauth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
    String errorMessage = exception.getMessage();
    log.error("OAuth2 Authentication Failure : {}", errorMessage);

    // 오류 메시지를 URL 인코딩하여 리디렉션 URL에 포함
    String encodedErrorMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);

    // 에러 페이지 또는 로그인 페이지로 리디렉션
    // 프론트엔드 애플리케이션의 오류 처리 페이지 URL로 변경 가능
    String redirectUrl = "/api/login?error=" + encodedErrorMessage;

    getRedirectStrategy().sendRedirect(request, response, redirectUrl);
  }
}
