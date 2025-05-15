package com.grimeet.grimeet.common.oauth;

public interface OAuthService {
    String generateAuthUrl(String username);
    void linkAccount(String username, String code, String state);
}
