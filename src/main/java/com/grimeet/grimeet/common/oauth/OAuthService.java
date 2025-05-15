package com.grimeet.grimeet.common.oauth;

public interface OAuthService {
    // 소셜 로그인할 url 생성
    String generateAuthUrl(String username);

    // 기존 계정과 소셜 계정 연결
    void linkAccount(String username, String code, String state);
}
