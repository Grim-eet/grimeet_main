package com.grimeet.grimeet.common.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  /**
   * SecurityFilterChain 빈 등록
   * 1. CSRF 비활성화 (Stateless 환경)
   * 2. 세션 생성 방지
   * 3. 요청에 대한 권한 설정
   * 4. 폼 로그인 설정
   * 5. Remember Me 설정 -> 쿠키 기반 자동 로그인(기본값 사용)
   * @param http
   * @return SecurityFilterChain
   * @throws Exception
   */
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
            .sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorizeRequests ->
                    authorizeRequests
                            .requestMatchers("/").permitAll()
                            .anyRequest().authenticated()
            )
            .formLogin(withDefaults())
            .rememberMe(withDefaults());

    return http.build();
  }
}
