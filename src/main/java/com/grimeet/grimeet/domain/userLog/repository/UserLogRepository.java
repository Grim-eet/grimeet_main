package com.grimeet.grimeet.domain.userLog.repository;

import com.grimeet.grimeet.domain.userLog.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserLogRepository extends JpaRepository<UserLog, Long> {
    UserLog findUserLogByUserId(Long userId);

    @Query("SELECT u FROM UserLog u WHERE u.nextNotificationDate = :now")
    List<UserLog> findAllByNextNotificationDateEqual(@Param("now") LocalDate now);

    @Query("SELECT u FROM UserLog u WHERE u.userId = :userId AND u.nextNotificationDate <= :now")
    UserLog findNextNotificationDateAfter(@Param("userId") Long id, @Param("now") LocalDate now);
}
