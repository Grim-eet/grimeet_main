package com.grimeet.grimeet.common.config.oauth.info;

public class KakaoOAuthUserInfo implements OAuthUserInfo {

  private final String id;
  private final String name;
  private final String email;

  public KakaoOAuthUserInfo(String id, String name, String email) {
    this.id = id;
    this.name = name;
    this.email = email;
  }

  @Override
  public String getProviderId() {
    return id;
  }

  @Override
  public String getProvider() {
    return "kakao";
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getEmail() {
    return email;
  }
}
