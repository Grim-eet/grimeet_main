package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.*;

public interface UserService {

    // 회원가입으로 유저 생성
    UserResponseDto createUser(UserCreateRequestDto requestDto);

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
}
