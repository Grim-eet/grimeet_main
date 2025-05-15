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

@Service
@RequiredArgsConstructor
public class KakaoOAuthServiceImpl implements OAuthService {

    private final OAuthClient oAuthClient;
    private final SocialAccountFacade socialAccountFacade;
    private final OAuthStateJwtProvider stateJwtProvider;

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
    public String generateAuthUrl(String username) {
        String state = stateJwtProvider.createStateToken(username, Provider.KAKAO.name());

        String scope = "profile_nickname,account_email";
        return AUTH_URI + "?client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&response_type=code"
                + "&state=" + state
                + "&scope=" + scope;
    }

    @Override
    public void linkAccount(String usernameFromLogin, String code, String state) {
        Claims claims = stateJwtProvider.validateStateToken(state);
        String providerFromState = claims.get("provider", String.class);
        String usernameFromState = stateJwtProvider.extractDecryptedUsername(claims);

        if (!Provider.KAKAO.name().equals(providerFromState) || !usernameFromLogin.equals(usernameFromState)) {
            throw new GrimeetException(ExceptionStatus.OAUTH2_INVALID_STATE);
        }

        OAuthConfig kakaoConfig = new OAuthConfig(
                clientId,
                clientSecret,
                redirectUri,
                TOKEN_URI,
                USER_INFO_URI
        );

        String accessToken = oAuthClient.getAccessToken(kakaoConfig, code, Provider.KAKAO);
        Map<String, Object> userInfo = oAuthClient.getUserInfo(USER_INFO_URI, accessToken);

        Object id = userInfo.get("id");
        if (id == null) {
            throw new GrimeetException(ExceptionStatus.OAUTH2_USERINFO_NOT_FOUND);
        }

        SocialAccountRequestDto dto = SocialAccountRequestDto.builder()
                .socialId(String.valueOf(id))
                .provider(Provider.KAKAO)
                .build();

        socialAccountFacade.linkSocialAccount(usernameFromLogin, dto);
    }
}
