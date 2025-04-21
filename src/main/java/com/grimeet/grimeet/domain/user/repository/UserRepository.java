package com.grimeet.grimeet.domain.user.repository;

import com.grimeet.grimeet.domain.user.dto.UserStatus;
import com.grimeet.grimeet.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    @Query("""
        SELECT u FROM User u
        WHERE u.id IN :ids
        AND u.userStatus IN :statuses
    """)
    List<User> findByIdInAndUserStatusIn(@Param("ids")List<Long> ids, @Param("statuses")List<UserStatus> statuses);

    Boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Boolean existsByPhoneNumber(String phoneNumber);

}
