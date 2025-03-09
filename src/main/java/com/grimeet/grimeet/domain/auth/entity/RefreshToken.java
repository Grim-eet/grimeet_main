package com.grimeet.grimeet.domain.auth.entity;


import com.grimeet.grimeet.common.entity.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class RefreshToken extends BaseTime {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_email", length = 255, nullable = false, unique = true, updatable = false)
  private String email;

  @Column(nullable = false, unique = true)
  private String token;
}
