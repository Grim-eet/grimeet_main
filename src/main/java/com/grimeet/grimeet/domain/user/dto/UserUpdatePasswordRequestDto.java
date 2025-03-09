package com.grimeet.grimeet.domain.user.dto;

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

    @NotBlank
    @Email
    @Size(min = 8, max = 200)
    private String email;

    @NotBlank
    @Size(min = 8, max = 200)
    private String password;

    @NotBlank
    @Size(min = 8, max = 200)
    private String newPassword;

    @NotBlank
    @Size(min = 8, max = 200)
    private String confirmPassword;

}
