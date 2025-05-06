package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.oauth.OAuthClient;
import com.grimeet.grimeet.common.oauth.OAuthConfig;
import com.grimeet.grimeet.common.oauth.OAuthService;
import com.grimeet.grimeet.domain.socialAccount.dto.Provider;
import com.grimeet.grimeet.domain.socialAccount.dto.SocialAccountRequestDto;
import com.grimeet.grimeet.domain.socialAccount.service.SocialAccountFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class NaverOAuthServiceImpl implements OAuthService {
    private final OAuthClient oAuthClient;
    private final SocialAccountFacade socialAccountFacade;

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String redirectUri;

    private static final String TOKEN_URI = "https://nid.naver.com/oauth2.0/token";
    private static final String USER_INFO_URI = "https://openapi.naver.com/v1/nid/me";
    private static final String AUTH_URI = "https://nid.naver.com/oauth2.0/authorize";

    @Override
    public String generateAuthUrl() {
        return AUTH_URI + "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code";
    }

    @Override
    public void linkAccount(String username, String code) {
        OAuthConfig naverConfig = new OAuthConfig(
                clientId,
                clientSecret,
                redirectUri,
                TOKEN_URI,
                USER_INFO_URI
        );

        String accessToken = oAuthClient.getAccessToken(naverConfig, code);
        Map<String, Object> userInfo = oAuthClient.getUserInfo(USER_INFO_URI, accessToken);

        Map<String, Object> response = (Map<String, Object>) userInfo.get("response");
        String naverId = String.valueOf(response.get("id"));

        SocialAccountRequestDto dto = SocialAccountRequestDto.builder()
                .socialId(naverId)
                .provider(Provider.NAVER)
                .build();

        socialAccountFacade.linkSocialAccount(username, dto);
    }
}
