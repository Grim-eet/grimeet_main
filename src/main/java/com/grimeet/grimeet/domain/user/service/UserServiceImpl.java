package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.image.ProfileImageDefaults;
import com.grimeet.grimeet.domain.upload.dto.ImageUploadResult;
import com.grimeet.grimeet.domain.upload.service.S3ImageService;
import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ImageService s3ImageService;

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

    @Transactional
    @Override
    public UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        verifyCurrentPasswordMatches(requestDto.getCurrentPassword(), user.getPassword());

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        return new UserResponseDto(user);
    }

    @Transactional
    @Override
    public UserResponseDto updateUserProfileImage(UserUpdateProfileImageRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
        MultipartFile image = requestDto.getImage();

        s3ImageService.deleteImageFromS3(user.getProfileImageKey());

        ImageUploadResult imageUploadResult = s3ImageService.upload(image);

        user.setProfileImageUrl(imageUploadResult.getUrl());
        user.setProfileImageKey(imageUploadResult.getKey());

        return new UserResponseDto(user);
    }

    @Transactional
    @Override
    public void deleteUserProfileImage(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        s3ImageService.deleteImageFromS3(user.getProfileImageKey());

        // 기본 이미지로 재설정
        user.setProfileImageUrl(ProfileImageDefaults.generateProfileImageUrl(user.getNickname()));
        user.setProfileImageKey(null);
    }

    private void verifyCurrentPasswordMatches(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new GrimeetException(ExceptionStatus.INVALID_PASSWORD);
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
