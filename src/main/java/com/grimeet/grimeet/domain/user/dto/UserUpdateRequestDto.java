package com.grimeet.grimeet.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {
    @Email
    @NotBlank
    private String email;

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    private String nickname;

    private String phoneNumber;
}
