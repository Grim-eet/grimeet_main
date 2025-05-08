package com.grimeet.grimeet.domain.websocket.draw_data.controller;

import com.grimeet.grimeet.domain.websocket.draw_data.dto.DrawDataRequestDto;
import com.grimeet.grimeet.domain.websocket.draw_data.service.DrawDataService;
import com.grimeet.grimeet.domain.websocket.service.WebSocketUserResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DrawDataWebSocketController {

  private final DrawDataService drawDataService;
  private final WebSocketUserResolver userResolver;

  @MessageMapping("/draw")
  public void receiveDrawData(DrawDataRequestDto requestDto,
                              Message<?> message) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    String userEmail = userResolver.resolveUserEmail(accessor);

    log.info("✅ Draw data received from user: {}", userEmail);
    log.info("✅ Draw data received: {}", requestDto);

    drawDataService.saveDrawData(requestDto, userEmail);
  }
}
