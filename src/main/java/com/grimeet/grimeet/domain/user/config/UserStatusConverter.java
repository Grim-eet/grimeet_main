package com.grimeet.grimeet.domain.user.config;

import com.grimeet.grimeet.domain.user.dto.UserStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Converter(autoApply = true)  // autoApply = true: UserStatus 타입 필드에 Converter 자동 적용 (선택 사항)
public class UserStatusConverter implements AttributeConverter<UserStatus, Integer> {
    @Override
    public Integer convertToDatabaseColumn(UserStatus userStatus) {
        return (userStatus == null) ? null : userStatus.getStatusCode();
    }

    @Override
    public UserStatus convertToEntityAttribute(Integer data) {
        if(data == null) {
            return null;
        }

        try {
            return UserStatus.valueOfStatusCode(data); // Enum의 statusCode를 기반으로 Enum 객체로 변환
        } catch (IllegalArgumentException e) {
            // 데이터베이스에 저장된 값이 유효한 statusCode가 아닐 경우 예외 처리 또는 default 값 반환
            // 예외 처리 방식은 프로젝트 요구사항에 따라 결정 (IllegalArgumentException 던지거나, default Enum 값 반환 등)
            log.warn("데이터베이스에 유효하지 않은 UserStatus 코드가 저장되어 있습니다. code: {}", data);
            return null; // 또는 UserStatus.NORMAL; 와 같이 default Enum 값 반환
        }
    }
}
