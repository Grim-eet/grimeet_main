package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserUpdatePasswordRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserResponseDto createUser(UserCreateRequestDto requestDto);

    Optional<User> findUserByUserId(Long userId);
    Optional<User> findUserByEmail(String email);
    Optional<User> fineUserByNickname(String nickname);

    List<User> findAllUsers();

    UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto);

//  void updateUser(UserUpdateRequestDto userUpdateRequestDto, Long userId);

    // 휴면 계정으로 전환
    void updateDormantUser(Long userId);
    // 탈퇴 회원으로 전환
    void updateWithdrawUser(Long userId);
    // User 삭제
    void deleteUser(Long userId);
}
