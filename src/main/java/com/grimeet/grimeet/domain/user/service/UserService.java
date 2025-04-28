package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.*;

import java.util.List;

public interface UserService {

    // email로 유저 찾기
    UserResponseDto findUserByEmail(Long id);

    // 탈퇴 회원으로 전환
    void updateUserStatusWithdrawal(String email);

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

    // 유저 아이디(이메일) 찾기
    String findUserEmailByNameAndPhoneNumber(UserFindEmailRequestDto requestDto);

    // 유저 비밀번호 찾기
    void findUserPasswordByEmail(UserFindPasswordRequestDto requestDto);
}
