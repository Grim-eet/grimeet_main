package com.grimeet.grimeet.domain.auth.entity;

import com.grimeet.grimeet.common.entity.BaseTime;
import com.grimeet.grimeet.domain.auth.dto.Provider;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "social_accounts")
@Getter
@NoArgsConstructor
public class SocialAccount extends BaseTime {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "social_id", length = 255, nullable = false)
  private String socialId;

  @Column(name = "provider", nullable = false)
  @Enumerated(EnumType.STRING)
  private Provider provider;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Builder
  public SocialAccount(String socialId, Provider provider, Long userId) {
    this.socialId = socialId;
    this.provider = provider;
    this.userId = userId;
  }
}
