package com.grimeet.grimeet.domain.socialAccount.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.socialAccount.dto.Provider;
import com.grimeet.grimeet.domain.socialAccount.dto.SocialAccountRequestDto;
import com.grimeet.grimeet.domain.socialAccount.dto.SocialAccountResponseDto;
import com.grimeet.grimeet.domain.socialAccount.entity.SocialAccount;
import com.grimeet.grimeet.domain.socialAccount.repository.SocialAccountRepository;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.service.UserFacade;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Tag(name = "SocialAccount", description = "소셜 계정 API")
public class SocialAccountFacade {

  private final SocialAccountRepository socialAccountRepository;
  private final UserFacade userFacade;

  public SocialAccount findByProviderAndSocialIdOrThrow(Provider provider, String socialId) {
    return socialAccountRepository.findBySocialIdAndProvider(socialId, provider)
            .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
  }

  /**
   * 소셜 계정 연동
   *
   * @param username String, socialAccountRequestDto SocialAccountRequestDto
   * @return socialAccountResponseDto
   */
  public SocialAccountResponseDto linkSocialAccount(String username, SocialAccountRequestDto socialAccountRequestDto) {
    // 사용자 ID 조회
    UserResponseDto findUser = userFacade.findUserByEmail(username);
    // 이미 연동된 소셜 계정이 있는지 확인
    String socialId = socialAccountRequestDto.getSocialId();
    Provider provider = socialAccountRequestDto.getProvider();
    socialAccountRepository.findBySocialIdAndProvider(socialId, provider)
            .ifPresent(account -> {
              throw new GrimeetException(ExceptionStatus.SOCIAL_ACCOUNT_ALREADY_LINKED);
            });

    // 연동
    SocialAccount socialAccount = SocialAccount.builder()
            .provider(provider)
            .socialId(socialId)
            .userId(findUser.getId())
            .build();

    SocialAccount savedSocialAccount = socialAccountRepository.save(socialAccount);
    return SocialAccountResponseDto.builder()
            .socialId(savedSocialAccount.getSocialId())
            .provider(String.valueOf(provider))
            .build();
  }
}
