package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.config.oauth.info.GoogleOAuthUserInfo;
import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.socialAccount.dto.Provider;
import com.grimeet.grimeet.domain.socialAccount.dto.SocialAccountRequestDto;
import com.grimeet.grimeet.domain.socialAccount.service.SocialAccountFacade;
import com.grimeet.grimeet.domain.user.service.UserFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl {

  private final SocialAccountFacade socialAccountFacade;

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String clientId;
  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String clientSecret;
  @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
  private String redirectUri;

  public void linkeGoogleAccount(String username, String code) {
    String accessToken = fetchGoogleAccessToken(code);
    GoogleOAuthUserInfo googleUserInfo = fetchGoogleUserInfo(accessToken);

    boolean alreadyLinked = socialAccountFacade.existsByProviderAndSocialId(Provider.GOOGLE, googleUserInfo.getProviderId());
    if(alreadyLinked) throw new GrimeetException(ExceptionStatus.SOCIAL_ACCOUNT_ALREADY_LINKED);

    SocialAccountRequestDto socialAccountRequestDto = SocialAccountRequestDto.builder()
        .socialId(googleUserInfo.getProviderId())
        .provider(Provider.GOOGLE)
        .build();
    socialAccountFacade.linkSocialAccount(username, socialAccountRequestDto);
  }

  private String fetchGoogleAccessToken(String code) {
    String tokenUrl = "https://oauth2.googleapis.com/token";

    try {
      RestTemplate restTemplate = new RestTemplate();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

      MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
      params.add("code", code);
      params.add("client_id", clientId);
      params.add("client_secret", clientSecret);
      params.add("redirect_uri", redirectUri);
      params.add("grant_type", "authorization_code");

      HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

      ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);

      Map<String, Object> body = response.getBody();
      if (body == null || !body.containsKey("access_token")) {
        throw new GrimeetException(ExceptionStatus.OAUTH2_ACCESS_TOKEN_NOT_FOUND);
      }

      return (String) body.get("access_token");

    } catch (Exception e) {
      log.error("❌ [fetchGoogleAccessToken] Error fetching Google Access Token", e);
      throw new GrimeetException(ExceptionStatus.OAUTH2_ACCESS_TOKEN_FAILED);
    }
  }


  private GoogleOAuthUserInfo fetchGoogleUserInfo(String accessToken) {
    String userInfoUrl = "https://www.googleapis.com/oauth2/v2/userinfo";

    try {
      RestTemplate restTemplate = new RestTemplate();
      HttpHeaders headers = new HttpHeaders();
      headers.setBearerAuth(accessToken);

      HttpEntity<?> entity = new HttpEntity<>(headers);

      ResponseEntity<Map> response = restTemplate.exchange(
              userInfoUrl,
              HttpMethod.GET,
              entity,
              Map.class
      );

      Map<String, Object> userInfo = response.getBody();

      return new GoogleOAuthUserInfo(userInfo);

    } catch (Exception e) {
      log.error("❌ [fetchGoogleUserInfo] Error fetching Google User Info", e);
      throw new GrimeetException(ExceptionStatus.OAUTH2_USERINFO_NOT_FOUND);
    }
  }

  public String generateGoogleOAuth2Url() {
    String scope = "profile email";
    String responseType = "code";
    return "https://accounts.google.com/o/oauth2/v2/auth"
            + "?client_id=" + clientId
            + "&redirect_uri=" + redirectUri
            + "&response_type=" + responseType
            + "&scope=" + scope
            + "&access_type=offline";
  }

}
