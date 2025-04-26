package com.grimeet.grimeet.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 아이디(이메일) 찾기 요청 DTO")
public class UserFindEmailRequestDto {

    @Schema(description = "사용자 이름", example = "홍길동")
    @NotBlank
    String name;

    @Schema(description = "사용자 전화번호", example = "010-1234-5678")
    @NotBlank
    String phoneNumber;
}
