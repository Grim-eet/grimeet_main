package com.grimeet.grimeet.domain.user.dto;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {
    NORMAL(1, "NORMAL", "일반 회원"),  // 1: 일반회원
    SOCIAL(2, "SOCIAL", "소셜 회원"),  // 2: 소셜회원
    DORMANT(4, "DORMANT", "휴면 회원"), // 4: 휴면회원
    WITHDRAWAL(9, "WITHDRAWAL", "탈퇴회원"); // 9: 탈퇴회원

    private final int statusCode;
    private final String statusName;
    private final String description;

    public static UserStatus valueOfStatusCode(Integer data) {
        if (data == null) {
            return null;
        }

        for (UserStatus userStatus : UserStatus.values()) {
            if (userStatus.getStatusCode() == data) {
                return userStatus;
            }
        }

        throw new GrimeetException(ExceptionStatus.INVALID_USER_STATUS);
    }
}
