package com.grimeet.grimeet.common.util.websocket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketEventListener {

  private final RedisTemplate<String, String> redisTemplate;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = accessor.getSessionId();
    String userName = accessor.getUser() != null
            ? accessor.getUser().getName()
            : redisTemplate.opsForValue().get("session:" + accessor.getSessionId());
    String projectId = accessor.getFirstNativeHeader("project-id");


    if (projectId == null || projectId.isBlank()) projectId = "default";

    log.info("🟢 [WebSocketEventListener] WebSocket 연결됨: sessionId={}, userName={}, projectId={}", sessionId, userName, projectId);

    String redisKey = "project:" + projectId + ":users";
    if (!"Unknown User".equals(userName)) {
      redisTemplate.opsForSet().add(redisKey, userName);
      redisTemplate.opsForValue().set("session:" + sessionId, userName);
      redisTemplate.opsForValue().set("session:" + sessionId + ":projectId", projectId);
      log.info("🟢 Redis에 사용자 추가 및 매핑 완료: {} → {}, projectId={}", sessionId, userName, projectId);
    }
  }


  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = accessor.getSessionId();
    String sessionKey = "session:" + sessionId;
    String userEmail = redisTemplate.opsForValue().get(sessionKey);
    String projectId = redisTemplate.opsForValue().get(sessionKey + ":projectId");

    log.info("🟣 WebSocket 연결 해제됨: sessionId={}, userEmail={}, projectId={}", sessionId, userEmail, projectId);

    if (userEmail != null && projectId != null) {
      String redisKey = "project:" + projectId + ":users";
      redisTemplate.opsForSet().remove(redisKey, userEmail);
      redisTemplate.delete(sessionKey);
      redisTemplate.delete(sessionKey + ":projectId");
      log.info("🟣 Redis에서 사용자 제거 완료: {}", userEmail);
    } else {
      log.warn("❌ Redis에서 세션 정보 없음: {}", sessionId);
    }
  }

}