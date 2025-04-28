package com.grimeet.grimeet.common.config.oauth.info;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class OAuthAttributes {

  private static final String KEY_NAME = "name";
  private static final String KEY_EMAIL = "email";
  private static final String KEY_ID = "id";
  private static final String KEY_RESPONSE = "response";
  private static final String KEY_KAKAO_ACCOUNT = "kakao_account";
  private static final String KEY_PROFILE = "profile";
  private static final String KEY_NICKNAME = "nickname";


  private Map<String, Object> attributes;
  private String nameAttributeKey;
  private String name;
  private String email;
  private String socialId;

  public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
    if("naver".equals(registrationId)) {
      return ofNaver(KEY_ID, attributes);
    } else if("kakao".equals(registrationId)) {
      return ofKakao(KEY_ID, attributes);
    }
    return ofGoogle(userNameAttributeName, attributes);
  }

  private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
    return OAuthAttributes.builder()
            .name((String) attributes.get(KEY_NAME))
            .email((String) attributes.get(KEY_EMAIL))
            .socialId((String) attributes.get(KEY_ID))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }

  private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");

    return OAuthAttributes.builder()
            .name((String) response.get(KEY_NAME))
            .email((String) response.get(KEY_EMAIL))
            .socialId((String) response.get(KEY_ID))
            .attributes(response)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }

  private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
    Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
    Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");

    return OAuthAttributes.builder()
            .name((String) kakaoProfile.get(KEY_NICKNAME))
            .email((String) kakaoAccount.get(KEY_EMAIL))
            .socialId(String.valueOf(attributes.get(KEY_ID)))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
  }
}
