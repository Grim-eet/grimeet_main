package com.grimeet.grimeet.domain.socialAccount.repository;

import com.grimeet.grimeet.domain.socialAccount.dto.Provider;
import com.grimeet.grimeet.domain.socialAccount.entity.SocialAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SocialAccountRepository extends JpaRepository<SocialAccount, Long> {
    /**
     * 소셜 계정으로 유저 정보를 조회하는 메소드
     *
     * @param socialId String
     * @return Optional<SocialAccount>
     */
    Optional<SocialAccount> findBySocialId(String socialId);

    /**
     * 소셜 계정으로 유저 정보를 조회하는 메소드
     *
     * @param socialId String
     * @param provider Provider
     * @return Optional<SocialAccount>
     */
    Optional<SocialAccount> findBySocialIdAndProvider(String socialId, Provider provider);

  /**
   * userId로 소셜 계정 정보를 조회하는 메소드
   *
   * @param userId Long
   * @return Optional<SocialAccount>
   */
  Optional<SocialAccount> findByUserId(Long userId);
}
