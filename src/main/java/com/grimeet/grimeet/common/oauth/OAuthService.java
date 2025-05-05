package com.grimeet.grimeet.common.oauth;

public interface OAuthService {
    String generateAuthUrl();
    void linkAccount(String username, String code);
}
