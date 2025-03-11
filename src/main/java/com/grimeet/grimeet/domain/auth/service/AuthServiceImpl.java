package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.jwt.JwtUtil;
import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.auth.entity.RefreshToken;
import com.grimeet.grimeet.domain.auth.repository.RefreshTokenRepository;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserStatus;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.domain.user.service.UserDetailServiceImpl;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;
  private final UserDetailServiceImpl userDetailService;
  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public void register(UserCreateRequestDto userCreateRequestDto) {
    log.info("User Create Request : {}", userCreateRequestDto);
    verifyExistUser(userCreateRequestDto);

    String encryptedPassword = passwordEncoder.encode(userCreateRequestDto.getPassword());

    User createdUser = User.builder()
            .name(userCreateRequestDto.getName())
            .email(userCreateRequestDto.getEmail())
            .password(encryptedPassword)
            .nickname(userCreateRequestDto.getNickname())
            .phoneNumber(userCreateRequestDto.getPhoneNumber())
            .userStatus(UserStatus.NORMAL)
            .build();

    userRepository.save(createdUser);
  }

  private void verifyExistUser(UserCreateRequestDto userCreateRequestDto) {
    if(userRepository.existsByEmail(userCreateRequestDto.getEmail())) {
      throw new GrimeetException(ExceptionStatus.LOGIN_ID_ALREADY_EXISTS);
    }
    if(userRepository.existsByNickname(userCreateRequestDto.getNickname())) {
      throw new GrimeetException(ExceptionStatus.NICKNAME_ALREADY_EXISTS);
    }
  }

  /**
   * AccessToken을 생성하는 메서드
   * @param refreshToken
   * @return accessToken
   */
  @Override
  @Transactional
  public AuthResponseDto createAccessToken(String refreshToken) {
    if(!jwtUtil.validateRefreshToken(refreshToken)) {
      throw new GrimeetException(ExceptionStatus.INVALID_TOKEN);
    }

    String findUsername = jwtUtil.getUsernameFromRefreshToken(refreshToken);
    RefreshToken findRefreshToken = verifyExistRefreshTokenByUsername(findUsername);


    UserDetails findUserDetail = userDetailService.loadUserByUsername(findUsername);
    verifyExistUserToUseUserDetail(findUserDetail);

    String newAccessToken = jwtUtil.generateAccessToken((UserPrincipalDetails) findUserDetail);

    rotateRefreshToken(findUserDetail, findRefreshToken);

    return new AuthResponseDto(newAccessToken);
  }

  @Transactional
  public void rotateRefreshToken(UserDetails userDetails, RefreshToken refreshToken) {
    String newRefreshToken = jwtUtil.generateRefreshToken((UserPrincipalDetails) userDetails);
    refreshToken.updateToken(newRefreshToken);
  }

  @Override
  @Transactional
  public String login(UserLoginRequestDto userLoginRequestDto) {
    UserDetails findUserDetails = userDetailService.loadUserByUsername(userLoginRequestDto.getEmail());
    User findUser = verifyExistUserToUseUserDetail(findUserDetails);

    // 비밀번호 확인
    if(!passwordEncoder.matches(userLoginRequestDto.getPassword(), findUser.getPassword())) {
      throw new GrimeetException(ExceptionStatus.INVALID_USER_LOGIN_INFO);
    }

    // accessToken생성
    String accessToken = jwtUtil.generateAccessToken((UserPrincipalDetails) findUserDetails);

    // refreshToken생성
    String refreshToken = jwtUtil.generateRefreshToken((UserPrincipalDetails) findUserDetails);

    refreshTokenRepository.save(new RefreshToken(findUser.getEmail(), refreshToken));

    // accessToken 반환
    return accessToken;
  }

  @Override
  @Transactional
  public void logout(String refreshToken) {
    RefreshToken findRefreshToken = verifyExistRefreshTokenByRefreshToken(refreshToken);
    String findUsername = jwtUtil.getUsernameFromRefreshToken(findRefreshToken.getToken());
    User findUser = userRepository.findByEmail(findUsername).orElseThrow(() -> {
      throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
    });
    RefreshToken findUserRefreshToken = verifyExistRefreshTokenByUserInfo(findUser);

    findUserRefreshToken.updateToken("");
  }

  private RefreshToken verifyExistRefreshTokenByUserInfo(User findUser) {
    return refreshTokenRepository.findByEmail(findUser.getEmail()).orElseThrow(() -> {
      throw new GrimeetException(ExceptionStatus.INVALID_TOKEN);
    });
  }

  private RefreshToken verifyExistRefreshTokenByRefreshToken(String refreshToken) {
    return refreshTokenRepository.findByToken(refreshToken).orElseThrow(() -> {
      throw new GrimeetException(ExceptionStatus.INVALID_TOKEN);
    });
  }

  private User verifyExistUserToUseUserDetail(UserDetails findUserDetail) {
    return userRepository.findByEmail(findUserDetail.getUsername()).orElseThrow(() -> {
      throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
    });
  }

  private RefreshToken verifyExistRefreshTokenByUsername(String findUsername) {
    return refreshTokenRepository.findByEmail(findUsername).orElseThrow(() -> {
      throw new GrimeetException(ExceptionStatus.INVALID_TOKEN);
    });
  }
}
