package com.grimeet.grimeet.common.interceptor;

import com.grimeet.grimeet.common.constant.AuthConstants;
import com.grimeet.grimeet.common.util.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

  private final JwtUtil jwtUtil;
  private final RedisTemplate<String, String> redisTemplate;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
      log.info("✅ [JwtCannelInterceptor] STOMP CONNECT 요청 수신");

      String authHeader = accessor.getFirstNativeHeader(AuthConstants.AUTHORIZATION_HEADER);

      if (authHeader != null && authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
        String token = authHeader.substring(AuthConstants.BEARER_PREFIX.length());
        log.info("✅ [JwtCannelInterceptor] JWT 토큰: {}", token);

        if (jwtUtil.validateAccessToken(token)) {
          String userEmail = jwtUtil.getUsernameFromAccessToken(token);
          accessor.setUser(new UsernamePasswordAuthenticationToken(userEmail, null, List.of()));
          log.info("✅ [JwtCannelInterceptor] 인증된 사용자: {}", userEmail);

          String projectId = accessor.getFirstNativeHeader("project-id");
          if (projectId == null || projectId.isBlank()) projectId = "default";

          String sessionId = accessor.getSessionId();
          if (sessionId != null) {
            String sessionKey = "session:" + sessionId;
            String projectKey = sessionKey + ":projectId";
            String projectUserSetKey = "project:" + projectId + ":users";

            redisTemplate.opsForValue().set(sessionKey, userEmail);
            redisTemplate.opsForValue().set(projectKey, projectId); // ✅ projectId 저장 추가
            redisTemplate.opsForSet().add(projectUserSetKey, userEmail);

            log.info("✅ [JwtCannelInterceptor] Redis 저장 완료: sessionKey={}, value={}", sessionKey, userEmail);
            log.info("✅ [JwtCannelInterceptor] Redis 저장 완료: {} = {}", projectKey, projectId);
            log.info("✅ [JwtCannelInterceptor] Redis Set 추가: key={}, user={}", projectUserSetKey, userEmail);
          }
        } else {
          log.warn("❌ [JwtCannelInterceptor] 유효하지않은 JWT 토큰");
          throw new IllegalArgumentException("Invalid JWT token");
        }
      } else {
        log.warn("❌ [JwtCannelInterceptor] Authorization 헤더가 없거나 형식 오류");
        throw new IllegalArgumentException("Missing or invalid Authorization header");
      }
    }

    return message;
  }
}
