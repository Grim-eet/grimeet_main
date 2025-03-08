package com.grimeet.grimeet.common.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class PasswordEncoderConfig {
  /**
   * 비밀번호 암호화를 위한 BCryptPasswordEncoder 빈 등록
   * salt를 이용한 해시 알고리즘으로 암호화 -> 같은 비밀번호라도 다른 해시값을 가짐
   * @see org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
   * @return BCryptPasswordEncoder
   */
  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
