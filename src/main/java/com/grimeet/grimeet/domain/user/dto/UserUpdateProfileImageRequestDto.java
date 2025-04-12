package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateProfileImageRequestDto {

    @Email
    @NotBlank
    private String email;

    @NotNull
    private MultipartFile image;
}
