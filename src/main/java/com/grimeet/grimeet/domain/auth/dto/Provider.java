package com.grimeet.grimeet.domain.auth.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Provider {
  GOOGLE(1, "GOOGLE"),
  KAKAO(2, "KAKAO"),
  NAVER(3, "NAVER");

  private int providerId;
  private String provider;
}
