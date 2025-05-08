package com.grimeet.grimeet.common.oauth;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.socialAccount.dto.Provider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
@Component
public class OAuthClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken(OAuthConfig config, String code, Provider provider) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", config.getClientId());
        params.add("client_secret", config.getClientSecret());
        params.add("redirect_uri", config.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(config.getTokenUri(), request, Map.class);
            Map<String, Object> body = response.getBody();
            if (body == null || !body.containsKey("access_token")) {
                throw new GrimeetException(ExceptionStatus.OAUTH2_ACCESS_TOKEN_NOT_FOUND);
            }
            return (String) body.get("access_token");
        } catch (HttpClientErrorException e) {
            log.error("[OAuth] AccessToken 요청 실패: {}", provider.name(), e.getResponseBodyAsString());
            log.error("[OAuth] AccessToken 요청 실패 상태코드: {}", e.getStatusCode());
            log.error("[OAuth] AccessToken 요청 실패 응답 바디: {}", e.getResponseBodyAsString());
            throw new GrimeetException(ExceptionStatus.OAUTH2_ACCESS_TOKEN_NOT_FOUND);
        }
    }

    public Map<String, Object> getUserInfo(String userInfoUri, String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<Void> request = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(userInfoUri, HttpMethod.GET, request, Map.class);

            log.info("[OAuth] Kakao userInfo = {}", response.getBody());

            return response.getBody();
        } catch (HttpClientErrorException e) {
            log.error("[OAuth] 사용자 정보 요청 실패: {}", e.getMessage());
            throw new GrimeetException(ExceptionStatus.OAUTH2_USERINFO_NOT_FOUND);
        }
    }
}
