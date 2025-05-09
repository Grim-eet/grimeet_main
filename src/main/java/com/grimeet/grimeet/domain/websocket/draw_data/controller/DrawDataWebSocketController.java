package com.grimeet.grimeet.domain.websocket.draw_data.controller;

import com.grimeet.grimeet.domain.websocket.draw_data.dto.DrawDataRequestDto;
import com.grimeet.grimeet.domain.websocket.draw_data.service.DrawDataService;
import com.grimeet.grimeet.domain.websocket.service.WebSocketUserResolver;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
@Tag(name = "WebSocket for DrawData", description = "WebSocket을 통한 실시간 드로잉 API")
public class DrawDataWebSocketController {

  private final DrawDataService drawDataService;
  private final WebSocketUserResolver userResolver;
  private final SimpMessagingTemplate messagingTemplate;

  @Operation(summary = "WebSocket 드로잉 메시지 예시", description = "WebSocket을 통해 드로잉 데이터를 전송합니다.")
  @MessageMapping("/draw")
  public void receiveDrawData(@Valid DrawDataRequestDto requestDto,
                              Message<?> message) {
    StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    String userEmail = userResolver.resolveUserEmail(accessor);

    log.info("✅ Draw data received from user: {}", userEmail);
    log.info("✅ Draw data received: {}", requestDto);

    messagingTemplate.convertAndSend(
            "/topic/project/" + requestDto.getProjectId(),
            requestDto
    );
    drawDataService.saveDrawData(requestDto, userEmail);
  }
}
