package com.grimeet.grimeet.common.config.oauth.info;

public class NaverOAuthUserInfo implements  OAuthUserInfo {

  private final String id;
  private final String name;
  private final String email;

  public NaverOAuthUserInfo(String id, String name, String email) {
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
    return "naver";
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
