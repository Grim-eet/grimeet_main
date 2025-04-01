package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.validation.PasswordFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {

  @NotBlank
  @Size(min = 2, max = 50)
  private String name;

  @NotBlank
  @Email
  @Size(max = 200)
  private String email;

  @NotBlank
  @PasswordFormat
  private String password;

  @NotBlank
  @Size(min = 2, max = 50)
  private String nickname;

  @NotBlank
  @Size(max = 15)
  private String phoneNumber;

  public User toEntity(UserCreateRequestDto userCreateRequestDto) {
    return User.builder()
            .name(userCreateRequestDto.getName())
            .email(userCreateRequestDto.getEmail())
            .password(userCreateRequestDto.getPassword())
            .nickname(userCreateRequestDto.getNickname())
            .phoneNumber(userCreateRequestDto.getPhoneNumber())
            .build();
  }
}
