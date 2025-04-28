package com.grimeet.grimeet.common.config.oauth.info;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class GoogleOAuthUserInfo implements OAuthUserInfo {

  private final Map<String, Object> attributes;

  @Override
  public String getProviderId() {
    return (String) attributes.get("id");
  }

  @Override
  public String getProvider() {
    return "google";
  }

  @Override
  public String getName() {
    Object name = attributes.get("name");
    return name != null ? name.toString() : null;
  }

  @Override
  public String getEmail() {
    Object email = attributes.get("email");
    return email != null ? email.toString() : null;
  }
}
