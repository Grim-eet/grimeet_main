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
@Schema(description = "사용자 정보 조회 요청 DTO")
public class UserFindMyInfoRequestDto {

    @Schema(description = "이메일(사용자 계정)", example = "testUser@example.com")
    @Email
    @NotBlank
    private String email;
}
