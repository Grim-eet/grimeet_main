package com.grimeet.grimeet.domain.userLog.repository;

import com.grimeet.grimeet.domain.userLog.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    UserLog findUserLogByUserId(Long userId);
}
