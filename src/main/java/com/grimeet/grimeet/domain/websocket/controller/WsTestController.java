package com.grimeet.grimeet.domain.websocket.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WsTestController {

  @MessageMapping("/hello")   // 클라이언트가 "/app/hello"로 메시지를 보낼 때 이 메서드가 호출됨
  @SendTo("/topic/greetings") // 메시지를 "/topic/greetings"로 전송
  public String greeting(String message) {
    return "Hello from server: " + message;
  }
}
