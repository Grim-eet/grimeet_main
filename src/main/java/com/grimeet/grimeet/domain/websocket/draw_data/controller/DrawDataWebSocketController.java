package com.grimeet.grimeet.domain.websocket.draw_data.controller;

import com.grimeet.grimeet.domain.websocket.draw_data.dto.DrawDataRequestDto;
import com.grimeet.grimeet.domain.websocket.draw_data.service.DrawDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DrawDataWebSocketController {

  private final DrawDataService drawDataService;

  @MessageMapping("/draw")
  public void receiveDrawData(DrawDataRequestDto requestDto,
                              @AuthenticationPrincipal UserDetails userDetails) {
    String userEmail = userDetails.getUsername();

    log.info("✅ Draw data received from user: {}", userEmail);
    log.info("✅ Draw data received: {}", requestDto);

    drawDataService.saveDrawData(requestDto, userEmail);
  }
}
