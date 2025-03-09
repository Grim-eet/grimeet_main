package com.grimeet.grimeet.common.config.security;

import com.grimeet.grimeet.common.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenAuthenticationFilter tokenAuthenticationFilter;
  private final CorsConfig corsConfig;

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
                            .requestMatchers("/**").permitAll()
                            .anyRequest().authenticated()
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
