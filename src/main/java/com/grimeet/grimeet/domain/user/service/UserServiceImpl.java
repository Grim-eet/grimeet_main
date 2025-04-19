package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.image.ProfileImageUtils;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3ImageService s3ImageService;

    // email로 유저 찾기
    @Transactional
    @Override
    public User findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
        return user;
    }

    // 전체 유저 조회
    @Transactional
    @Override
    public List<UserCreateRequestDto> findAllUsers() {
        return List.of();
    }

    // 유저 상태 탈퇴 전환
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

    // 유저 상태 휴면 전환
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

    // 유저 상태 일반 전환
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
      
    // 유저 정보 업데이트
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

    // 유저 비밀번호 업데이트
    @Transactional
    @Override
    public UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        verifyCurrentPasswordMatches(requestDto.getCurrentPassword(), user.getPassword());

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

        return new UserResponseDto(user);
    }

    // 유저 프로필 이미지 변경
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

    // 유저 프로필 이미지 삭제
    @Transactional
    @Override
    public UserResponseDto deleteUserProfileImage(UserDeleteProfileImageRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        s3ImageService.deleteImageFromS3(user.getProfileImageKey());

        // 기본 이미지로 재설정
        user.setProfileImageUrl(ProfileImageUtils.generateProfileImageUrl(user.getNickname()));
        user.setProfileImageKey(null);

        return new UserResponseDto(user);
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
