package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.config.oauth.info.OAuthAttributes;
import com.grimeet.grimeet.domain.auth.repository.SocialAccountRepository;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {
  private final UserRepository userRepository;
  private final SocialAccountRepository socialAccountRepository;

  public CustomOAuth2UserServiceImpl(UserRepository userRepository, SocialAccountRepository socialAccountRepository) {
    super();
    this.userRepository = userRepository;
    this.socialAccountRepository = socialAccountRepository;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
            .getUserInfoEndpoint().getUserNameAttributeName();
    OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

    return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            oAuthAttributes.getAttributes(),
            oAuthAttributes.getNameAttributeKey()
    );
  }

  @Override
  public void setAttributesConverter(Converter<OAuth2UserRequest, Converter<Map<String, Object>, Map<String, Object>>> attributesConverter) {
    super.setAttributesConverter(attributesConverter);
  }
}
