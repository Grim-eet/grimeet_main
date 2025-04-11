package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.validation.PasswordFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 비밀번호 수정 요청 DTO")
public class UserUpdatePasswordRequestDto {

    @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
    @Email
    private String email;

    @Schema(description = "현재 비밀번호", example = "test1234!#")
    @PasswordFormat
    private String currentPassword;

    @Schema(description = "새로운 비밀번호", example = "NewPassword1234!@")
    @PasswordFormat
    private String newPassword;

}
