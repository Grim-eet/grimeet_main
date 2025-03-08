package com.grimeet.grimeet.domain.user.service;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
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
   * @param nickname
   * @return UserDetails
   * @throws UsernameNotFoundException
   */
  @Override
  public UserDetails loadUserByUsername(String nickname) throws UsernameNotFoundException {
    User findUser = findUserByEmailOrThrow(nickname);

    List<GrantedAuthority> authorities = Collections.singletonList((GrantedAuthority) () -> "ROLE_USER");
    return new UserPrincipalDetails(findUser, authorities);
  }

  private User findUserByEmailOrThrow(String nickname) {
    return userRepository.findByNickname(nickname)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다. nickname: " + nickname));
  }
}
