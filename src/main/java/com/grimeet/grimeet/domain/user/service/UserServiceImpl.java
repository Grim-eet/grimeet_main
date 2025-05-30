package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.domain.user.validation.PasswordFormat;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 유저 생성
    @Transactional
    @Override
    public UserResponseDto createUser(UserCreateRequestDto createRequestDto) {
        log.info("User Create Request : {}", createRequestDto);

        verifyUniqueEmail(createRequestDto.getEmail());
        verifyUniqueNickname(createRequestDto.getNickname());
        verifyUniquePhoneNumber(createRequestDto.getPhoneNumber());

        User createdUser = createRequestDto.toEntity(createRequestDto);
        User savedUser = userRepository.save(createdUser);

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

        // 닉네임 변경
        if (requestDto.getNickname() != null) {
            verifyUniqueNickname(requestDto.getNickname());
            user.setNickname(requestDto.getNickname());
        }

        // 전화번호 변경
        if (requestDto.getPhoneNumber() != null) {
            verifyUniquePhoneNumber(requestDto.getPhoneNumber());
            user.setPhoneNumber(requestDto.getPhoneNumber());
        }

        return new UserResponseDto(user);
    }

    @Override
    public UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        verifyCurrentPasswordMatches(requestDto.getCurrentPassword(), user.getPassword());

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        return new UserResponseDto(user);
    }

    private void verifyCurrentPasswordMatches(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_PASSWORD);
        }
    }

    private void verifyUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GrimeetException(ExceptionStatus.EMAIL_ALREADY_EXISTS);
        }
    }

    private void verifyUniqueNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    private void verifyUniquePhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new GrimeetException(ExceptionStatus.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

}
