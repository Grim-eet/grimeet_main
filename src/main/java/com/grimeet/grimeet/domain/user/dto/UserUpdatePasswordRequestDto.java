package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.validation.PasswordFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdatePasswordRequestDto {

    @Email
    private String email;

    @PasswordFormat
    private String currentPassword;

    @PasswordFormat
    private String newPassword;

}
