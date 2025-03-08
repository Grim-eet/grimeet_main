package com.grimeet.grimeet.common.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

  /**
   * CORS 설정
   * - 허용할 Origin, Method, Header 설정
   * - preflight 요청에 대한 응답 헤더 설정 (OPTIONS) - 1시간 캐싱으로 초기 셋팅
   * @return CorsConfigurationSource
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration corsConfiguration = new CorsConfiguration();

    corsConfiguration.setAllowCredentials(true);

    corsConfiguration.addAllowedOrigin("http://localhost:3000");
    corsConfiguration.addAllowedOrigin("http://localhost:5173");
    corsConfiguration.addAllowedOriginPattern("https://*.grimeet.com");

    corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    corsConfiguration.addExposedHeader("Authorization");
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.setMaxAge(3600L);

    source.registerCorsConfiguration("/**", corsConfiguration);

    return source;
  }
}
