package com.grimeet.grimeet.common.config.oauth.info;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleOAuthUserInfo implements OAuthUserInfo {

  private final Map<String, Object> attributes;

  @Override
  public String getProviderId() {
    return (String) attributes.get("sub");
  }

  @Override
  public String getProvider() {
    return "google";
  }

  @Override
  public String getName() {
    return (String) attributes.get("name");
  }

  @Override
  public String getEmail() {
    return (String) attributes.get("email");
  }
}
