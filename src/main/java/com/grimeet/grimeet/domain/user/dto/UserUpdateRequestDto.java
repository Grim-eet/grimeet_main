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

    private String currentPassword; // 회원정보 수정 로직(화면) 확인 후 검토
    private String newPassword;
    private String confirmPassword;

    private String nickname;

    private String phoneNumber;
}
