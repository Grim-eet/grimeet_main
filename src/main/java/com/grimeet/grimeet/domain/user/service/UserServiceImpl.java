package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.*;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
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

    @Override
    public Optional<User> findUserByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        Optional<User> userByEmail = userRepository.findByEmail(email);
        if (userByEmail.isEmpty()) {
            throw new IllegalArgumentException("해당 유저를 찾을 수 없습니다.");
        }
        return userByEmail;
    }

    @Override
    public Optional<User> fineUserByNickname(String nickname) {
        return Optional.empty();
    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Transactional
    @Override
    public UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto) {
        User user = findUserByEmail(requestDto.getEmail()).get();

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
        User user = findUserByEmail(requestDto.getEmail()).get();

        verifyUserStatus(user.getUserStatus());
        verifySameNickname(user.getNickname(), requestDto.getNewNickname());
        verifyUniqueNickname(requestDto.getNewNickname());

        user.setNickname(requestDto.getNewNickname());
    }

    @Override
    public void updateDormantUser(String email) {

    }

    @Transactional
    @Override
    public void updateWithdrawUser(String email) {
        User user = findUserByEmail(email).get();
        user.setUserStatus(UserStatus.WITHDRAWAL);

    }

    @Override
    public void deleteUser(String email) {

    }

    private void verifyExistsEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    private void verifyExistsNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
    }

    private void verifyExistsPhoneNumber(String phoneNumber) {
        if (userRepository.existsByNickname(phoneNumber)) {
            throw new IllegalArgumentException("이미 존재하는 전화번호입니다.");
        }
    }

    private void verifyCurrentPassword(String currentPassword, String inputPassword) {
        if (!currentPassword.equals(inputPassword)) {
            throw new IllegalArgumentException("비밀번호를 잘못 입력했습니다.");
        }
    }

    private void verifyNewPassword(String currentPassword, String newPassword) {
        if (currentPassword.equals(newPassword)) {
            throw new IllegalArgumentException("기존과 다른 비밀번호를 입력하세요.");
        }
    }

    private void verifyConfirmPassword(String newPassword, String confirmPassword) {
        if (!confirmPassword.equals(newPassword)) {
            throw new IllegalArgumentException("입력하신 비밀번호와 다릅니다.");
        }
    }

    private void verifyUserStatus(UserStatus userStatus) {
        if (userStatus == UserStatus.WITHDRAWAL) {
            throw new IllegalArgumentException("탈퇴 회원은 비밀번호 변경이 불가합니다.");
        }
    }

    private void verifySameNickname(String oldNickname, String newNickname) {
        if (oldNickname.equals(newNickname)) {
            throw new IllegalArgumentException("기존과 같은 닉네임입니다.");
        }
    }

    private void verifyUniqueNickname(String newNickname) {
        Optional<User> user = userRepository.findByNickname(newNickname);
        if (!user.isEmpty()) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }
    }
}
