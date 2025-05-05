package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.*;

public interface UserService {

    // email로 유저 찾기
    UserResponseDto findUserByEmail(String email);

    // 탈퇴 회원으로 전환
    void updateUserStatusWithdrawal(String email);

    // 일반 회원으로 전환
    void updateUserStatusNormal(String email);

    // 유저 정보 업데이트
    UserResponseDto updateUserInfo(String email, UserUpdateRequestDto requestDto);

    // 유저 비밀번호 업데이트
    UserResponseDto updateUserPassword(String email, UserUpdatePasswordRequestDto requestDto);

    // 유저 프로필 이미지 변경
    UserResponseDto updateUserProfileImage(String email, UserUpdateProfileImageRequestDto requestDto);

    // 유저 프로필 이미지 삭제
    UserResponseDto deleteUserProfileImage(String email);

    // 유저 아이디(이메일) 찾기
    String findUserEmailByNameAndPhoneNumber(UserFindEmailRequestDto requestDto);

    // 유저 비밀번호 찾기
    void findUserPasswordByEmail(UserFindPasswordRequestDto requestDto);
}
