package com.grimeet.grimeet.common.config.oauth.info;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {
  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String socialId;

  public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    if("naver".equals(registrationId)) {
      return ofNaver("id", attributes);
    } else if("kakao".equals(registrationId)) {
      return ofKakao("id", attributes);
    }
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
            .name((String) attributes.get("name"))
            .email((String) attributes.get("email"))
            .socialId((String) attributes.get(userNameAttributeName))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }

  private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuthAttributes.builder()
            .name((String) response.get("name"))
            .email((String) response.get("email"))
            .socialId((String) response.get("id"))
            .attributes(response)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }

  private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuthAttributes.builder()
            .name((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get("email"))
            .socialId(String.valueOf(attributes.get("id")))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }
}
