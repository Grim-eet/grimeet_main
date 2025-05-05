package com.grimeet.grimeet.common.oauth;

import lombok.Getter;

/**
 * OAuth 인증 요청에 필요한 설정값을 저장하는 클래스
 * - clientId: 클라이언트 식별자
 * - clientSecret: 클라이언트 시크릿
 * - redirectUri: 인증 후 돌아올 URI
 * - tokenUri: 액세스 토큰 요청 URL
 * - userInfoUri: 사용자 정보 요청 URL
 */
@Getter
public class OAuthConfig {

    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUri;
    private final String userInfoUri;

    public OAuthConfig(String clientId, String clientSecret, String redirectUri, String tokenUri, String userInfoUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userInfoUri = userInfoUri;
    }
}
