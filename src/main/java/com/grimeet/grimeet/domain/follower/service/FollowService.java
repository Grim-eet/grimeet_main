package com.grimeet.grimeet.domain.follower.service;

import com.grimeet.grimeet.domain.user.entity.User;

import java.util.List;

public interface FollowService {

  void follow(Long followerId, Long followingId);         // 팔로우 수행
  boolean isFollowing(Long followerId, Long followingId); // 팔로우 여부 확인

  List<User> getFollowers(Long userId);   // 나를 팔로우한 사람 목록
  List<User> getFollowings(Long userId);  // 내가 팔로우한 사람 목록
}
