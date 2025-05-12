package com.grimeet.grimeet.domain.user_log.repository;

import com.grimeet.grimeet.domain.user.dto.UserStatus;
import com.grimeet.grimeet.domain.user_log.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface    UserLogRepository extends JpaRepository<UserLog, Long> {
    UserLog findUserLogByUserId(Long userId);

    List<UserLog> findByNextDormantCheckDateLessThanEqual(LocalDate date);

    @Query("""
    SELECT ul FROM UserLog ul
    JOIN User u ON ul.userId = u.id
    WHERE ul.nextDormantCheckDate <= :now
    AND u.userStatus IN (:activeStatuses)
    """)
    List<UserLog> findCandidatesForDormantCheck(@Param("now") LocalDate now, @Param("activeStatuses") List<UserStatus> activeStatuses);


    @Query("SELECT u FROM UserLog u WHERE u.userId = :userId AND u.nextNotificationDate <= :now")
    UserLog findNextNotificationDateAfter(@Param("userId") Long id, @Param("now") LocalDate now);
}
