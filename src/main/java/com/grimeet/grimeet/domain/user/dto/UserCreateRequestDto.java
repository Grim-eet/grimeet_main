package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.common.image.ProfileImageDefaults;
import io.swagger.v3.oas.annotations.media.Schema;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.validation.PasswordFormat;
import jakarta.validation.constraints.*;
        import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "신규 사용자 생성 요청 DTO")
public class UserCreateRequestDto {

  @Schema(description = "사용자 이름", example = "홍길동")
  @NotBlank
  @Size(min = 2, max = 50)
  private String name;

  @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
  @NotBlank
  @Email
  private String email;

  @Schema(description = "비밀번호", example = "test1234!#")
  @NotBlank
  @PasswordFormat
  private String password;

  @Schema(description = "닉네임", example = "둘리")
  @NotBlank
  @Size(min = 2, max = 50)
  private String nickname;

  @Schema(description = "전화번호", example = "010-1234-5678")
  @NotBlank
  @Size(max = 15)
  private String phoneNumber;

  public User toEntity(UserCreateRequestDto userCreateRequestDto, String encryptedPassword) {
    return User.builder()
            .name(userCreateRequestDto.getName())
            .email(userCreateRequestDto.getEmail())
            .password(encryptedPassword)
            .nickname(userCreateRequestDto.getNickname())
            .phoneNumber(userCreateRequestDto.getPhoneNumber())
            .userStatus(UserStatus.NORMAL)
            .profileImageUrl(ProfileImageDefaults.generateProfileImageUrl(userCreateRequestDto.getNickname()))
            .profileImageKey(null)
            .build();
  }
}
