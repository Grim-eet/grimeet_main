package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
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

    @Transactional
    @Override
    public UserResponseDto createUser(UserCreateRequestDto createRequestDto) {
        log.info("User Create Request : {}", createRequestDto);

        verifyExistsEmail(createRequestDto.getEmail());
        verifyExistsNickname(createRequestDto.getNickname());
        verifyExistsPhoneNumber(createRequestDto.getPhoneNumber());

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

        verifyUserStatus(user.getUserStatus());

        verifyCurrentPassword(user.getPassword(), requestDto.getPassword());
        verifyNewPassword(user.getPassword(), requestDto.getNewPassword());
        verifyConfirmPassword(requestDto.getNewPassword(), requestDto.getConfirmPassword());

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

        verifyUserStatus(user.getUserStatus());
        verifySameNickname(user.getNickname(), requestDto.getNickname());
        verifyUniqueNickname(requestDto.getNickname());

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

        verifyUserStatus(user.getUserStatus());
        verifySamePhoneNumber(user.getPhoneNumber(), requestDto.getPhoneNumber());
        verifyUniquePhoneNumber(requestDto.getPhoneNumber());

        user.setPhoneNumber(requestDto.getPhoneNumber());
    }

    private void verifyExistsEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GrimeetException(ExceptionStatus.EMAIL_ALREADY_EXISTS);
        }
    }

    private void verifyExistsNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    private void verifyExistsPhoneNumber(String phoneNumber) {
        if (userRepository.existsByNickname(phoneNumber)) {
            throw new GrimeetException(ExceptionStatus.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    private void verifyCurrentPassword(String currentPassword, String inputPassword) {
        if (!currentPassword.equals(inputPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_USER_LOGIN_INFO);
        }
    }

    private void verifyNewPassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_PASSWORD);
        }
    }

    private void verifyConfirmPassword(String newPassword, String confirmPassword) {
        if (!confirmPassword.equals(newPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_PASSWORD);
        }
    }

    private void verifyUserStatus(UserStatus userStatus) {
        if (userStatus == UserStatus.WITHDRAWAL) {
            throw new GrimeetException(ExceptionStatus.INVALID_ROLE);
        }
    }

    private void verifySameNickname(String oldNickname, String newNickname) {
        if (oldNickname.equals(newNickname)) {
            throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    private void verifyUniqueNickname(String newNickname) {
        if (userRepository.existsByNickname(newNickname)) {
            throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    private void verifySamePhoneNumber(String oldPhoneNumber, String newPhoneNumber) {
        if (oldPhoneNumber.equals(newPhoneNumber)) {
            throw new GrimeetException(ExceptionStatus.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    private void verifyUniquePhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new GrimeetException(ExceptionStatus.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

}
