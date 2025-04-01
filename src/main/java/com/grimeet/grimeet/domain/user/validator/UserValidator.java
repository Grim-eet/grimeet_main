package com.grimeet.grimeet.domain.user.validator;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.UserStatus;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserRepository userRepository;

    public void verifyExistsEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new GrimeetException(ExceptionStatus.EMAIL_ALREADY_EXISTS);
        }
    }

    public void verifyExistsNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    public void verifyExistsPhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new GrimeetException(ExceptionStatus.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    public void verifyCurrentPassword(String currentPassword, String inputPassword) {
        if (!currentPassword.equals(inputPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_USER_LOGIN_INFO);
        }
    }

    public void verifyNewPassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_PASSWORD);
        }
    }

    public void verifyConfirmPassword(String newPassword, String confirmPassword) {
        if (!confirmPassword.equals(newPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_PASSWORD);
        }
    }

    public void verifyUserStatus(UserStatus userStatus) {
        if (userStatus == UserStatus.WITHDRAWAL) {
            throw new GrimeetException(ExceptionStatus.INVALID_ROLE);
        }
    }

    public void verifySameNickname(String oldNickname, String newNickname) {
        if (oldNickname.equals(newNickname)) {
            throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    public void verifyUniqueNickname(String newNickname) {
        if (userRepository.existsByNickname(newNickname)) {
            throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
        }
    }

    public void verifySamePhoneNumber(String oldPhoneNumber, String newPhoneNumber) {
        if (oldPhoneNumber.equals(newPhoneNumber)) {
            throw new GrimeetException(ExceptionStatus.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }

    public void verifyUniquePhoneNumber(String phoneNumber) {
        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            throw new GrimeetException(ExceptionStatus.PHONE_NUMBER_ALREADY_EXISTS);
        }
    }
}
