package com.grimeet.grimeet.domain.user.dto;


import com.grimeet.grimeet.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String profileImageUrl;
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
}