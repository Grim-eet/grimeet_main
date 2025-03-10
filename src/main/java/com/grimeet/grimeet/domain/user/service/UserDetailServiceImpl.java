package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * 사용자 정보를 조회하여 UserDetails 객체를 반환.
   * @param userEmail
   * @return UserDetails
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
    User findUser = findUserByEmailOrThrow(userEmail);

    List<GrantedAuthority> authorities = Collections.singletonList((GrantedAuthority) () -> "ROLE_USER");
    return new UserPrincipalDetails(findUser, authorities);
  }

  private User findUserByEmailOrThrow(String userEmail) {
    return userRepository.findByNickname(userEmail)
            .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
  }
}
