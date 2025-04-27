package com.grimeet.grimeet.domain.socialAccount.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.socialAccount.dto.Provider;
import com.grimeet.grimeet.domain.socialAccount.entity.SocialAccount;
import com.grimeet.grimeet.domain.socialAccount.repository.SocialAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocialAccountFacade {

  private final SocialAccountRepository socialAccountRepository;

  public SocialAccount findByProviderAndSocialIdOrThrow(Provider provider, String socialId) {
    return socialAccountRepository.findBySocialIdAndProvider(socialId, provider.name())
            .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
  }

  /**
   * 소셜 계정으로 유저 정보를 조회하는 메소드
   *
   * @param socialId String
   * @return Optional<SocialAccount>
   */
  public SocialAccount saveSocialAccount(Provider provider, String socialId, Long userId) {
    SocialAccount socialAccount = SocialAccount.builder()
            .provider(provider)
            .socialId(socialId)
            .userId(userId)
            .build();
    return socialAccountRepository.save(socialAccount);
  }
}
