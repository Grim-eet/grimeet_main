package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.domain.user.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserValidator userValidator;

    // 유저 생성
    @Transactional
    @Override
    public UserResponseDto createUser(UserCreateRequestDto createRequestDto) {
        log.info("User Create Request : {}", createRequestDto);

        userValidator.verifyExistsEmail(createRequestDto.getEmail());
        userValidator.verifyExistsNickname(createRequestDto.getNickname());
        userValidator.verifyExistsPhoneNumber(createRequestDto.getPhoneNumber());

        User createUser = createRequestDto.toEntity(createRequestDto);
        User savedUser = userRepository.save(createUser);

        return new UserResponseDto(savedUser);
    }

    // 유저 상태(일반, 휴면, 탈퇴) 업데이트
    @Transactional
    @Override
    public void updateUserStatusWithdrawal(String email) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        user.setUserStatus(UserStatus.WITHDRAWAL);
    }

    @Transactional
    @Override
    public void updateUserStatusDormant(String email) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        user.setUserStatus(UserStatus.DORMANT);
    }

    @Transactional
    @Override
    public void updateUserStatusNormal(String email) {
        Optional<User> optionalUser = userRepository.findUserByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        user.setUserStatus(UserStatus.NORMAL);
    }

    // 이메일로 유저 찾기
    @Transactional
    @Override
    public UserResponseDto findUserByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        return new UserResponseDto(user);
    }

    // 유저 상태 업데이트
    @Transactional
    @Override
    public UserResponseDto updateUserInfo(UserUpdateRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        // 비밀번호 변경
        if (requestDto.getNewPassword() != null) {
            userValidator.verifyCurrentPassword(user.getPassword(), requestDto.getCurrentPassword());
            userValidator.verifyNewPassword(user.getPassword(), requestDto.getNewPassword());
            userValidator.verifyConfirmPassword(requestDto.getNewPassword(), requestDto.getConfirmPassword());

            user.setPassword(requestDto.getNewPassword());
        }

        // 닉네임 변경
        if (requestDto.getNickname() != null) {
            userValidator.verifySameNickname(user.getNickname(), requestDto.getNickname());
            userValidator.verifyUniqueNickname(requestDto.getNickname());

            user.setNickname(requestDto.getNickname());
        }

        // 전화번호 변경
        if (requestDto.getPhoneNumber() != null) {
            userValidator.verifySamePhoneNumber(user.getPhoneNumber(), requestDto.getPhoneNumber());
            userValidator.verifyUniquePhoneNumber(requestDto.getPhoneNumber());

            user.setPhoneNumber(requestDto.getPhoneNumber());
        }

        return new UserResponseDto(user);
    }

}
