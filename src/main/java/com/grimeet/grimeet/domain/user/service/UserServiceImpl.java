package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserUpdatePasswordRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
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

    @Transactional
    @Override
    public UserResponseDto updateUserPassword(UserUpdatePasswordRequestDto requestDto) {
        User user = findUserByEmail(requestDto.getEmail()).get();

        verifyCurrentPassword(user.getPassword(), requestDto.getPassword());
        verifyNewPassword(user.getPassword(), requestDto.getNewPassword());
        verifyConfirmPassword(requestDto.getNewPassword(), requestDto.getConfirmPassword());

        user.setPassword(requestDto.getNewPassword());

        return new UserResponseDto(user);
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

    @Override
    public void updateDormantUser(Long userId) {

    }

    @Override
    public void updateWithdrawUser(Long userId) {

    }

    @Override
    public void deleteUser(Long userId) {

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
}
