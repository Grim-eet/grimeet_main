package com.grimeet.grimeet.domain.user.repository;

import com.grimeet.grimeet.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 이메일로 유저 정보를 조회하는 메서드
     * @param email
     * @return Optional<User>
     */
    Optional<User> findUserByEmail(String email); // Optional을 사용하면 null 체크를 하지 않아도 된다. NullPointException을 방지할 수 있다.

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Boolean existsByPhoneNumber(String phoneNumber);

}
