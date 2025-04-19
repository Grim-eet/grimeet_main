package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    // 탈퇴 회원으로 전환
    void updateUserStatusWithdrawal(String email);

    // 휴면 회원으로 전환
    void updateUserStatusDormant(String email);

    // 일반 회원으로 전환
    void updateUserStatusNormal(String email);

    // email로 유저 찾기
    UserResponseDto findUserByEmail(String email);

    // 유저 정보 업데이트
    UserResponseDto updateUserInfo(UserUpdateRequestDto requestDto);

    // 유저 비밀번호 업데이트
    UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto);

    // 유저 프로필 이미지 변경
    UserResponseDto updateUserProfileImage(UserUpdateProfileImageRequestDto requestDto);

    // 유저 프로필 이미지 삭제
    void deleteUserProfileImage(String email);

}
