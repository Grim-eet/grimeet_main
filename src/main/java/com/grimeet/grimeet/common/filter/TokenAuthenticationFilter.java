package com.grimeet.grimeet.common.filter;

import com.grimeet.grimeet.common.jwt.JwtUtil;
import com.grimeet.grimeet.domain.user.service.UserDetailServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailServiceImpl userDetailsService;

  /**
   * Request Header에서 Token을 추출하여 인증을 수행한다. -> 추출한 Token이 유효하면 SecurityContextHolder에 인증 정보를 저장한다.
   * Username은 nickname으로 설정한다.
   * @param request
   * @param response
   * @param filterChain
   * @throws ServletException
   * @throws IOException
   */
  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    try {
      String token = jwtUtil.resolveToken(request);

      if (token != null && jwtUtil.validateAccessToken(token)) {
        String username = jwtUtil.getUsernameFromAccessToken(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (userDetails != null) {
          UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } else {
          logger.error("User not found for username extracted from token: " + username);
          SecurityContextHolder.clearContext(); // 이 경우 인증 비우기
        }
      }

    } catch (Exception e) {
      // 토큰 처리 중 발생한 예외를 로깅하지만, 필터 체인은 계속 진행
      logger.error("Cannot set user authentication: " + e);

      SecurityContextHolder.clearContext(); // 인증 컨텍스트 초기화
    }
    filterChain.doFilter(request, response);
  }
}
