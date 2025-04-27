package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.image.ProfileImageUtils;
import com.grimeet.grimeet.common.mail.service.MailService;
import com.grimeet.grimeet.common.util.user.TempPasswordGenerator;
import com.grimeet.grimeet.domain.upload.dto.ImageUploadResult;
import com.grimeet.grimeet.domain.upload.service.S3ImageService;
import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.domain.userLog.service.UserLogFacade;
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
    private final UserLogFacade userLogFacade;
    private final S3ImageService s3ImageService;
    private final MailService mailService;
    private final TempPasswordGenerator tempPasswordGenerator; // 임시 비밀번호 생성기

    /**
     * 이메일로 사용자 조회
     * 조회 성공 시 사용자 정보 반환
     * @param requestDto
     * @return UserResonseDto(user)
     */
    @Transactional
    @Override
    public UserResponseDto findUserByEmail(UserFindMyInfoRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
        return new UserResponseDto(user);
    }

    // 유저 상태 탈퇴 전환
    @Transactional
    @Override
    public void updateUserStatusWithdrawal(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        user.setUserStatus(UserStatus.WITHDRAWAL);
    }

    @Transactional
    @Override
    public void updateUserStatusDormantBatch(List<Long> ids) {
        try {
            List<User> users = userRepository.findByIdInAndUserStatusIn(ids, List.of(UserStatus.NORMAL, UserStatus.SOCIAL));  // 한번에 조회

            // 일반, 소셜 회원만 조회 -> 휴면 전환
            int successCount = 0;

            for (User user : users) {
                if (user.getUserStatus() == UserStatus.NORMAL || user.getUserStatus() == UserStatus.SOCIAL) {
                    user.setUserStatus(UserStatus.DORMANT);
                    successCount++;
                }
            }

            log.info("[UserService] 휴면 처리 완료 → 조회: 총 {}, 휴면 전환 성공: {}", users.size(), successCount);
        } catch (Exception e) {
            log.info("[UserService] 휴면 상태 일괄 전환 중 예외 발생: {}", e.getMessage());
            // 알림이나 감지 필요 시 추가
        }

    }

    // 유저 상태 일반 전환
    @Transactional
    @Override
    public void updateUserStatusNormal(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

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
        userLogFacade.updatePasswordLog(user.getId());

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

    /**
     * 기존 사용자의 계정(이메일)을 이름, 전화번호로 찾는다.
     * @param requestDto
     * @return email
     */
    @Transactional
    @Override
    public String findUserEmailByNameAndPhoneNumber(UserFindEmailRequestDto requestDto) {
        return userRepository.findEmailByNameAndPhoneNumber(requestDto.getName(), requestDto.getPhoneNumber())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
    }

    /**
     * 사용자 비밀번호 찾기
     * 임시 비밀번호를 사용자 이메일로 발급하여 전송한다.
     * 임시 비밀번호는 사용자 비밀번호로 저장된다.
     * @param requestDto
     */
    @Transactional
    @Override
    public void findUserPasswordByEmail(UserFindPasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));

        // 임시 비밀빈호 발급
        String tempPassword = tempPasswordGenerator.generate();
        // 사용자 비밀번호 변경
        user.setPassword(passwordEncoder.encode(tempPassword));
        userLogFacade.updatePasswordLog(user.getId());

        // 이메일 발송
        mailService.sendEmail(user.getEmail(), "[Grimeet] 임시 비밀번호 발급", "[임시 비밀번호] " + tempPassword);
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
