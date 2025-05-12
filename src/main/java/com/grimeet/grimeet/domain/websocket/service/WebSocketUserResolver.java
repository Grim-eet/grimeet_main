package com.grimeet.grimeet.domain.websocket.service;

import com.grimeet.grimeet.common.exception.ExceptionStatus;
import com.grimeet.grimeet.common.exception.GrimeetException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketUserResolver {

  private final RedisTemplate<String, String> redisTemplate;

  public String resolveUserEmail(StompHeaderAccessor accessor) {
    if (accessor.getUser() != null) {
      return accessor.getUser().getName();
    }

    String sessionId = accessor.getSessionId();
    if (sessionId != null) {
      String userEmail = redisTemplate.opsForValue().get("session:" + sessionId);
      if (userEmail != null) {
        return userEmail;
      }
      log.warn("🟥 Unknown session ID in Redis lookup: {}", sessionId);
    }

    throw new GrimeetException(ExceptionStatus.UNAUTHORIZED_USER);
  }
}

