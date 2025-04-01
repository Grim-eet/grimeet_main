package com.grimeet.grimeet.common.config.oauth.info;

public interface OAuthUserInfo {
  String getProviderId();
  String getProvider();
  String getEmail();
  String getName();
}
