package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

@Getter
@Schema(description = "사용자 응답 DTO")
public class UserResponseDto {

    @Schema(description = "사용자 고유 ID", example = "1")
    private Long id;

    @Schema(description = "이름", example = "홍길동")
    private String name;

    @Schema(description = "이메일(로그인 아이디)", example = "testUser@example.com")
    private String email;

    @Schema(description = "닉네임", example = "둘리")
    private String nickname;

    @Schema(description = "전화번호", example = "010-1234-5678")
    private String phoneNumber;

    @Schema(description = "프로필 이미지 Url", example = "profileImage")
    private String profileImageUrl;

    @Schema(description = "프로필 이미지 key")
    private String profileImageKey;

    @Schema(description = "사용자 상태 (NORMAL: 일반, SOCIAL: 소셜, DORMANT: 휴면, WITHDRAWAL: 탈퇴", example = "NORMAL")
    private UserStatus userStatus;

    @Builder
    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.phoneNumber = user.getPhoneNumber();
        this.profileImageUrl = user.getProfileImageUrl();
        this.userStatus = user.getUserStatus();
    }

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .nickname(this.nickname)
                .phoneNumber(this.phoneNumber)
                .profileImageUrl(this.profileImageUrl)
                .userStatus(this.userStatus)
                .build();
    }
}
