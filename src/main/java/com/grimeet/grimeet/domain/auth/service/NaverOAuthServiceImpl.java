package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.oauth.OAuthClient;
import com.grimeet.grimeet.common.oauth.OAuthConfig;
import com.grimeet.grimeet.common.oauth.OAuthService;
import com.grimeet.grimeet.common.oauth.OAuthStateJwtProvider;
import com.grimeet.grimeet.domain.socialAccount.dto.Provider;
import com.grimeet.grimeet.domain.socialAccount.dto.SocialAccountRequestDto;
import com.grimeet.grimeet.domain.socialAccount.service.SocialAccountFacade;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NaverOAuthServiceImpl implements OAuthService {

    private final OAuthClient oAuthClient;
    private final SocialAccountFacade socialAccountFacade;
    private final OAuthStateJwtProvider stateJwtProvider;

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
    public String generateAuthUrl(String username) {
        String state = stateJwtProvider.createStateToken(username, Provider.NAVER.name());

        return AUTH_URI + "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code"+
                "&state=" + state +
                "&scope=name%20email";
    }

    @Override
    public void linkAccount(String usernameFromLogin, String code, String state) {
        // 1. state 검증
        Claims claims = stateJwtProvider.validateStateToken(state);
        Provider providerFromState = Provider.valueOf(claims.get("provider", String.class));
        String usernameFromState = stateJwtProvider.extractDecryptedUsername(claims);

        if (providerFromState != Provider.NAVER || !usernameFromLogin.equals(usernameFromState)) {
            throw new GrimeetException(ExceptionStatus.OAUTH2_INVALID_STATE);
        }

        OAuthConfig naverConfig = new OAuthConfig(
                clientId,
                clientSecret,
                redirectUri,
                TOKEN_URI,
                USER_INFO_URI
        );

        String accessToken = oAuthClient.getAccessToken(naverConfig, code, Provider.NAVER);
        Map<String, Object> userInfo = oAuthClient.getUserInfo(USER_INFO_URI, accessToken);

        Object rawResponse = userInfo.get("response");
        if (!(rawResponse instanceof Map)) {
            throw new GrimeetException(ExceptionStatus.OAUTH2_USERINFO_NOT_FOUND);
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> response = (Map<String, Object>) rawResponse;

        String naverId = String.valueOf(response.get("id"));
        if (naverId == null) {
            throw new GrimeetException(ExceptionStatus.OAUTH2_USERINFO_NOT_FOUND);
        }

        SocialAccountRequestDto dto = SocialAccountRequestDto.builder()
                .socialId(naverId)
                .provider(Provider.NAVER)
                .build();

        socialAccountFacade.linkSocialAccount(usernameFromLogin, dto);
    }
}
