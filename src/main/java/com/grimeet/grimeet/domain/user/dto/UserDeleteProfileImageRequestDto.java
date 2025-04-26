package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 프로필 이미지 삭제 요청 DTO")
public class UserDeleteProfileImageRequestDto {

    @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
    @Email
    @NotBlank
    private String email;

}
