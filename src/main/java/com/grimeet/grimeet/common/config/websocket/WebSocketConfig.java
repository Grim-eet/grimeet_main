package com.grimeet.grimeet.common.config.websocket;

import com.grimeet.grimeet.common.interceptor.JwtCannelInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  private final JwtCannelInterceptor jwtCannelInterceptor;

  /**
   * WebSocket 연결을 위한 엔드포인트를 등록합니다.
   * @param registry
   */
  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")  // 추후 배포 시 허용할 도메인 설정 필요
            .withSockJS();  // SockJS를 사용하여 WebSocket을 지원하지 않는 브라우저에서도 사용할 수 있도록 설정
  }

  /**
   * 메시지 브로커를 설정합니다.
   * @param registry
   */
  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic", "/queue");  // 클라이언트가 구독할 수 있는 주제 설정
    registry.setApplicationDestinationPrefixes("/app"); // 클라이언트가 서버로 메시지를 보낼 때 사용할 접두사 설정
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(jwtCannelInterceptor);
  }
}
