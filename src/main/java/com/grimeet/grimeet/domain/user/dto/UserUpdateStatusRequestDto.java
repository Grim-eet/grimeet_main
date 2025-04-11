package com.grimeet.grimeet.domain.user.dto;

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
@Schema(description = "사용자 상태 수정 요청 DTO")
public class UserUpdateStatusRequestDto {

    @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
    @NotBlank
    @Email
    private String email;

}
