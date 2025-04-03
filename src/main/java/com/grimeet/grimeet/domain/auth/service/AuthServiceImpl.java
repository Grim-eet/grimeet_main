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
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
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
  public UserResponseDto register(UserCreateRequestDto userCreateRequestDto) {
    log.info("User Create Request : {}", userCreateRequestDto);
    verifyExistUser(userCreateRequestDto);

    String encryptedPassword = passwordEncoder.encode(userCreateRequestDto.getPassword());

    String profileUrl = "https://api.dicebear.com/9.x/notionists-neutral/svg?seed=";

    User createdUser = User.builder()
            .name(userCreateRequestDto.getName())
            .email(userCreateRequestDto.getEmail())
            .password(encryptedPassword)
            .nickname(userCreateRequestDto.getNickname())
            .phoneNumber(userCreateRequestDto.getPhoneNumber())
            .userStatus(UserStatus.NORMAL)
            .profileImageUrl(profileUrl + userCreateRequestDto.getNickname())
            .build();

    userRepository.save(createdUser);

    return new UserResponseDto(createdUser);
  }

  private void verifyExistUser(UserCreateRequestDto userCreateRequestDto) {
    if(userRepository.existsByEmail(userCreateRequestDto.getEmail())) {
      throw new GrimeetException(ExceptionStatus.EMAIL_ALREADY_EXISTS);
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


    refreshTokenRepository.findByEmail(findUser.getEmail()).ifPresentOrElse(
            findRefreshToken -> { // 이미 Refresh Token이 있으면 업데이트
              findRefreshToken.updateToken(refreshToken);
              refreshTokenRepository.save(findRefreshToken);
            },
            () -> { // Refresh Token이 없으면 새로 생성
              RefreshToken newRefreshToken = new RefreshToken(findUser.getEmail(), refreshToken); //userId사용하는 생성자로 변경
              refreshTokenRepository.save(newRefreshToken);
            }
    );

    // accessToken 반환
    return accessToken;
  }

  @Override
  @Transactional
  public void logout(String userEmail) {
    RefreshToken findRefreshToken = verifyExistRefreshTokenByUsername(userEmail);
    String findUsername = jwtUtil.getUsernameFromRefreshToken(findRefreshToken.getToken());
    verifyExistUsername(findUsername);

    findRefreshToken.updateToken("");
  }

  private void verifyExistUsername(String findUsername) {
    userRepository.findByEmail(findUsername).orElseThrow(() -> {
      throw new GrimeetException(ExceptionStatus.USER_NOT_FOUND);
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
