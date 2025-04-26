package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 비밀번호 찾기 요청 DTO")
public class UserFindPasswordRequestDto {

    @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
    @Email
    @NotBlank
    private String email;

}
