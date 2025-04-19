package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.validation.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 프로필 이미지 수정 요청 DTO")
public class UserUpdateProfileImageRequestDto {

    @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
    @Email
    @NotBlank
    private String email;

    @Schema(description = "새로운 프로필 이미지(form-data로 전송)", type = "string", format = "binary")
    @NotNull
    private MultipartFile image;

}
