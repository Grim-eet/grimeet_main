package com.grimeet.grimeet.domain.websocket.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

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
    }

    throw new IllegalStateException("User is not authenticated.");
  }
}

