package com.grimeet.grimeet.domain.follower.repository;

import com.grimeet.grimeet.domain.follower.entity.Follower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowerRepository extends JpaRepository<Follower, Long> {

    Optional<Follower> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    List<Follower> findAllByFollowingId(Long userId);
    List<Follower> findAllByFollowerId(Long userId);
    boolean existsByFollowerIdAndFollowingId(Long followerId, Long followingId);
    void deleteByFollowerIdAndFollowingId(Long followerId, Long followingId);
}
