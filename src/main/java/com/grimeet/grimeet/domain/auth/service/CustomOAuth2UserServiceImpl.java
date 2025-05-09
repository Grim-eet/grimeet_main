package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.config.oauth.info.OAuthAttributes;
import com.grimeet.grimeet.domain.social_account.dto.Provider;
import com.grimeet.grimeet.domain.social_account.service.SocialAccountFacade;
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
@RequiredArgsConstructor
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {

  private final SocialAccountFacade socialAccountFacade;


  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    OAuthAttributes attributes = OAuthAttributes.of(
            userRequest.getClientRegistration().getRegistrationId(),
            userRequest.getClientRegistration().getProviderDetails()
                    .getUserInfoEndpoint().getUserNameAttributeName(),
            oAuth2User.getAttributes()
    );

    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    String socialId = attributes.getSocialId();

    socialAccountFacade.findByProviderAndSocialIdOrThrow(
            Provider.valueOf(registrationId.toUpperCase()),
            socialId
    );


    return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            attributes.getAttributes(),
            attributes.getNameAttributeKey()
    );
  }

  @Override
  public void setAttributesConverter(Converter<OAuth2UserRequest, Converter<Map<String, Object>, Map<String, Object>>> attributesConverter) {
    super.setAttributesConverter(attributesConverter);
  }
}
