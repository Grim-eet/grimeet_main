package com.grimeet.grimeet.domain.auth.service;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.util.jwt.JwtUtil;
import com.grimeet.grimeet.domain.auth.dto.AuthResponseDto;
import com.grimeet.grimeet.domain.auth.dto.TokenRefreshResponseDto;
import com.grimeet.grimeet.domain.auth.dto.UserLoginRequestDto;
import com.grimeet.grimeet.domain.auth.entity.RefreshToken;
import com.grimeet.grimeet.domain.auth.repository.RefreshTokenRepository;
import com.grimeet.grimeet.domain.user.dto.UserCreateRequestDto;
import com.grimeet.grimeet.domain.user.dto.UserResponseDto;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import com.grimeet.grimeet.domain.user.service.UserDetailServiceImpl;
import com.grimeet.grimeet.domain.userLog.service.UserLogFacade;
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
  private final UserLogFacade userLogFacade;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public UserResponseDto register(UserCreateRequestDto userCreateRequestDto) {
    log.info("User Create Request : {}", userCreateRequestDto);
    verifyEmailAuthPass(userCreateRequestDto);
    verifyExistUser(userCreateRequestDto);

    String encryptedPassword = passwordEncoder.encode(userCreateRequestDto.getPassword());

    User createdUser = userCreateRequestDto.toEntity(userCreateRequestDto, encryptedPassword);

    userRepository.save(createdUser);
    // 회원가입 시 사용자 로그 생성
    userLogFacade.createUserLog(createdUser.getId());

    return new UserResponseDto(createdUser);
  }

  private void verifyEmailAuthPass(UserCreateRequestDto userCreateRequestDto) {
    if (Boolean.FALSE.equals(userCreateRequestDto.getIsPassedEmailAuth())) {
      throw new GrimeetException(ExceptionStatus.INVALID_USER_EMAIL_AUTH);
    }
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
  public TokenRefreshResponseDto createAccessToken(String refreshToken) {
    if(!jwtUtil.validateRefreshToken(refreshToken)) {
      throw new GrimeetException(ExceptionStatus.INVALID_TOKEN);
    }

    String findUsername = jwtUtil.getUsernameFromRefreshToken(refreshToken);
    RefreshToken findRefreshToken = verifyExistRefreshTokenByUsername(findUsername);


    UserDetails findUserDetail = userDetailService.loadUserByUsername(findUsername);
    verifyExistUserToUseUserDetail(findUserDetail);

    String newAccessToken = jwtUtil.generateAccessToken((UserPrincipalDetails) findUserDetail);

    String newRefreshToken = rotateRefreshToken(findUserDetail, findRefreshToken);

    return new TokenRefreshResponseDto(newAccessToken, newRefreshToken);
  }

  @Transactional
  public String rotateRefreshToken(UserDetails userDetails, RefreshToken refreshToken) {
    String newRefreshToken = jwtUtil.generateRefreshToken((UserPrincipalDetails) userDetails);
    refreshToken.updateToken(newRefreshToken);
    refreshTokenRepository.save(refreshToken);
    return newRefreshToken;
  }

  @Override
  @Transactional
  public AuthResponseDto login(UserLoginRequestDto userLoginRequestDto) {
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


    String savedRefreshToken = saveOrUpdateRefreshToken(findUser, refreshToken);

    // 비밀번호 변경 권장 알림 체크
    boolean checkChangePasswordRequired = userLogFacade.checkNotificationRequired(findUser.getId());
    // 휴면 계정 여부 체크
    boolean checkDormantAccount = userLogFacade.checkDormantUserLog(findUser.getId());

    // accessToken 반환
    return AuthResponseDto.builder()
            .accessToken(accessToken)
            .refreshToken(savedRefreshToken)
            .isPasswordChangeRequired(checkChangePasswordRequired)
            .isDormant(checkDormantAccount)
            .build();
  }

  private String saveOrUpdateRefreshToken(User findUser, String newRefreshToken) {
    return refreshTokenRepository.findByEmail(findUser.getEmail())
            .map(existingToken -> {
              existingToken.updateToken(newRefreshToken);
              refreshTokenRepository.save(existingToken);
              return existingToken.getToken(); // 반환
            })
            .orElseGet(() -> {
              RefreshToken createdToken = new RefreshToken(findUser.getEmail(), newRefreshToken);
              refreshTokenRepository.save(createdToken);
              return createdToken.getToken();
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
