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

}
