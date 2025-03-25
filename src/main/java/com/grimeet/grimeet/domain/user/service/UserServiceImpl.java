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

    // 유저 상태 수정

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

    @Transactional
    @Override
    public UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        userValidator.verifyUserStatus(user.getUserStatus());
        userValidator.verifyCurrentPassword(user.getPassword(), requestDto.getPassword());
        userValidator.verifyNewPassword(user.getPassword(), requestDto.getNewPassword());
        userValidator.verifyConfirmPassword(requestDto.getNewPassword(), requestDto.getConfirmPassword());

        user.setPassword(requestDto.getNewPassword());

        return new UserResponseDto(user);
    }

    @Transactional
    @Override
    public void updateUserNickname(UserUpdateNicknameRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findUserByEmail(requestDto.getEmail());

        if (optionalUser.isEmpty()) {
            throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        userValidator.verifyUserStatus(user.getUserStatus());
        userValidator.verifySameNickname(user.getNickname(), requestDto.getNickname());
        userValidator.verifyUniqueNickname(requestDto.getNickname());

        user.setNickname(requestDto.getNickname());
    }

    @Transactional
    @Override
    public void updateUserPhoneNumber(UserUpdatePhoneNumberRequestDto requestDto) {
        Optional<User> optionalUser = userRepository.findUserByEmail(requestDto.getEmail());

        if (optionalUser.isEmpty()) {
            throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();

        userValidator.verifyUserStatus(user.getUserStatus());
        userValidator.verifySamePhoneNumber(user.getPhoneNumber(), requestDto.getPhoneNumber());
        userValidator.verifyUniquePhoneNumber(requestDto.getPhoneNumber());

        user.setPhoneNumber(requestDto.getPhoneNumber());
    }

}
