package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.*;

import java.util.List;

public interface UserService {

    // email로 유저 찾기
    UserResponseDto findUserByEmail(String email);

    // 전체 유저 조회
    List<User> findAllUsers();

    // 탈퇴 회원으로 전환
    void updateUserStatusWithdrawal(String email);

    // 휴면 회원으로 전환
    void updateUserStatusDormant(String email);

    // 스케줄러 -> 휴면 회원들로 전환
    void updateUserStatusDormantBatch(List<Long> ids);

    // 일반 회원으로 전환
    void updateUserStatusNormal(String email);

    // 유저 정보 업데이트
    UserResponseDto updateUserInfo(UserUpdateRequestDto requestDto);

    // 유저 비밀번호 업데이트
    UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto);

    // 유저 프로필 이미지 변경
    UserResponseDto updateUserProfileImage(UserUpdateProfileImageRequestDto requestDto);

    // 유저 프로필 이미지 삭제
    UserResponseDto deleteUserProfileImage(UserDeleteProfileImageRequestDto requestDto);

}
