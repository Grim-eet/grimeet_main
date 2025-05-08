package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
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
public class KakaoOAuthServiceImpl implements OAuthService {

    private final OAuthClient oAuthClient;
    private final SocialAccountFacade socialAccountFacade;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;

    private static final String TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String AUTH_URI = "https://kauth.kakao.com/oauth/authorize";

    @Override
    public String generateAuthUrl() {
        String responseType = "code";
        String scope = "profile_nickname,account_email";
        return AUTH_URI + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=" + responseType
                + "&scope=" + scope;
    }

    @Override
    public void linkAccount(String username, String code) {
        OAuthConfig kakaoConfig = new OAuthConfig(
                clientId,
                clientSecret,
                redirectUri,
                TOKEN_URI,
                USER_INFO_URI
        );

        String accessToken = oAuthClient.getAccessToken(kakaoConfig, code, Provider.KAKAO);
        Map<String, Object> userInfo = oAuthClient.getUserInfo(USER_INFO_URI, accessToken);

        if (userInfo == null || !userInfo.containsKey("id")) {
            throw new GrimeetException(ExceptionStatus.OAUTH2_USERINFO_NOT_FOUND);
        }

        String kakaoId = String.valueOf(userInfo.get("id"));

        SocialAccountRequestDto dto = SocialAccountRequestDto.builder()
                .socialId(kakaoId)
                .provider(Provider.KAKAO)
                .build();

        socialAccountFacade.linkSocialAccount(username, dto);
    }
}
