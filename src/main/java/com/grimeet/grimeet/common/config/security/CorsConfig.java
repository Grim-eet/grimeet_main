package com.grimeet.grimeet.common.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
public class CorsConfig {

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

    source.registerCorsConfiguration("/**", corsConfiguration);

    return source;
  }
}
