package com.grimeet.grimeet.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @NotBlank
    @Email
    private String email;

    private String currentPassword; // 수정 로직 확인 후 수정 예정
    private String newPassword;
    private String confirmPassword;

    private String nickname;

    private String phoneNumber;
}
