package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
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

    @Override
    public Optional<UserCreateRequestDto> findUserByUserId(Long userId) {
        return Optional.empty();
    }

    @Override
    public Optional<UserCreateRequestDto> findUserByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Optional<UserCreateRequestDto> fineUserByNickname(String nickname) {
        return Optional.empty();
    }

    @Override
    public UserResponseDto findUserByUserEmail(String email) {
        Optional<UserResponseDto> findUser = userRepository.findByEmail(email);
        return findUser.isPresent() ? findUser.get() : null;
    }

    @Override
    public List<UserCreateRequestDto> findAllUsers() {
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
}
