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

    private String nickname;

    private String phoneNumber;
}
