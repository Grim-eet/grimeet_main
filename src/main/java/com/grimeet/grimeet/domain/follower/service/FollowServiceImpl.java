package com.grimeet.grimeet.domain.follower.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.domain.follower.entity.Follower;
import com.grimeet.grimeet.domain.follower.repository.FollowerRepository;
import com.grimeet.grimeet.domain.user.entity.User;
import com.grimeet.grimeet.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FollowerServiceImpl implements FollowerService {

  private final FollowerRepository followerRepository;
  private final UserRepository userRepository;

  @Override
  public void follow(Long followerId, Long followingId) {
    // 팔로우 시도 시 자기 자신을 팔로우하는 경우 예외 발생(혹시나..)
    checkTryToFollowSelf(followerId, followingId);

    // 이미 팔로우 중인 관계라면 언팔로우 수행
    if (checkFollowingNow(followerId, followingId)) return;

    User follower = isAlreadyFollowingAndUnfollowIfTrue(followerId);
    User following = isAlreadyFollowingAndUnfollowIfTrue(followingId);

    Follower newFollow = Follower.builder()
            .follower(follower)
            .following(following)
            .build();

    followerRepository.save(newFollow);
  }

  @Override
  public void unfollow(Long followerId, Long followingId) {
    followerRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
  }

  @Override
  public boolean isFollowing(Long followerId, Long followingId) {
    return followerRepository.existsByFollowerIdAndFollowingId(followerId, followingId);
  }

  @Override
  public List<User> getFollowers(Long userId) {
    return followerRepository.findAllByFollowingId(userId).stream()
            .map(Follower::getFollower)
            .collect(Collectors.toList());
  }

  @Override
  public List<User> getFollowings(Long userId) {
    return followerRepository.findAllByFollowerId(userId).stream()
            .map(Follower::getFollowing)
            .collect(Collectors.toList());
  }

  private User isAlreadyFollowingAndUnfollowIfTrue(Long followerId) {
    return userRepository.findById(followerId)
            .orElseThrow(() -> new GrimeetException(ExceptionStatus.USER_NOT_FOUND));
  }

  private boolean checkFollowingNow(Long followerId, Long followingId) {
    Optional<Follower> existing = followerRepository.findByFollowerIdAndFollowingId(followerId, followingId);

    if (existing.isPresent()) {
      // 이미 팔로우된 상태면 언팔로우 수행
      followerRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
      return true;
    }
    return false;
  }

  private static void checkTryToFollowSelf(Long followerId, Long followingId) {
    if (followerId.equals(followingId)) {
      throw new GrimeetException(ExceptionStatus.INVALID_FOLLOW);
    }
  }
}
