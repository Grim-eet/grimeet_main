package com.grimeet.grimeet.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "사용자 유저 정보 수정 요청 DTO (비밀번호 제외)")
public class UserUpdateRequestDto {

    @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
    @NotBlank
    @Email
    private String email;

    @Schema(description = "닉네임", example = "둘리")
    private String nickname;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;
}
