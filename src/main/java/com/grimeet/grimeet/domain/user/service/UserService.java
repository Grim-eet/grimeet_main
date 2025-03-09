package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDto createUser(UserCreateRequestDto requestDto);

    Optional<User> findUserByUserId(Long userId);
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByNickname(String nickname);
    Optional<User> findUserByPhoneNumber(String phoneNumber);

    List<User> findAllUsers();

    UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto);

    void updateUserNickname(UserUpdateNicknameRequestDto requestDto);
    void updateUserPhoneNumber(UserUpdatePhoneNumberRequestDto requestDto);

    // 휴면 계정으로 전환
    void updateDormantUser(String email);
    // 탈퇴 회원으로 전환
    void updateWithdrawUser(String email);
    // User 삭제
    void deleteUser(String email);

}
