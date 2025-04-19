package com.grimeet.grimeet.domain.user.repository;

import com.grimeet.grimeet.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    List<User> findByIdIn(List<Long> ids);

    Boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Boolean existsByPhoneNumber(String phoneNumber);

}
