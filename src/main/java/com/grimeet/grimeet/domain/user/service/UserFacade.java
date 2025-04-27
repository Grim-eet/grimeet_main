package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

  private final UserRepository userRepository;

  public UserResponseDto findUserByEmail(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
    return new UserResponseDto(user);
  }
}
