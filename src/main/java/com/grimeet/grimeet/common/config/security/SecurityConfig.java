package com.grimeet.grimeet.common.config.security;

import com.grimeet.grimeet.common.filter.TokenAuthenticationFilter;
import com.grimeet.grimeet.common.jwt.JwtUtil;
import com.grimeet.grimeet.domain.auth.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenAuthenticationFilter tokenAuthenticationFilter;
  private final CorsConfig corsConfig;
  private final RefreshTokenRepository refreshTokenRepository;
  private final JwtUtil jwtUtil;

  /**
   * AuthenticationManager 빈 등록
   * @param configuration
   * @return AuthenticationManager
   * @throws Exception
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }

  /**
   * SecurityFilterChain 빈 등록
   * 1. CSRF 비활성화 (Stateless 환경)
   * 2. 세션 생성 방지
   * 3. 요청에 대한 권한 설정
   * 4. 폼 로그인 설정
   * 5. UsernamePasswordAuthenticationFilter 앞에 TokenAuthenticationFilter 추가
   * @param http
   * @return SecurityFilterChain
   * @throws Exception
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/auth/logout").authenticated()
                            .requestMatchers("/**").permitAll()
                            .anyRequest().authenticated()
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(logout -> logout
                    .logoutUrl("/auth/logout")
                    .logoutSuccessHandler((request, response, authentication) -> {
                      try {
                        // 토큰에서 직접 사용자 이메일 추출 시도
                        String token = jwtUtil.resolveToken(request);
                        if (token != null && jwtUtil.validateAccessToken(token)) {
                          String userEmail = jwtUtil.getUsernameFromAccessToken(token);

                          // 기존 로그아웃 로직 수행
                          refreshTokenRepository.findByEmail(userEmail)
                                  .ifPresent(refreshToken -> {
                                    refreshToken.updateToken("");
                                    refreshTokenRepository.save(refreshToken);
                                  });

                          // 쿠키 삭제
                          removeCookie(response);

                          response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                          return;
                        }

                        removeCookie(response);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                      } catch (Exception e) {
                        removeCookie(response);
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                      }
                    })

            )
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  private static void removeCookie(HttpServletResponse response) {
    Cookie cookie = new Cookie("Authorization_Access", null);
    cookie.setMaxAge(0);
    cookie.setPath("/");
    response.addCookie(cookie);
  }
}
