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

    // 비밀번호 업데이트
    UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto);

    // 닉네임 업데이트
    void updateUserNickname(UserUpdateNicknameRequestDto requestDto);

    // 전화번호 업데이트
    void updateUserPhoneNumber(UserUpdatePhoneNumberRequestDto requestDto);

}
