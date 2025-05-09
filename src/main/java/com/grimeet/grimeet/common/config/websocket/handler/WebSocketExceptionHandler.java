package com.grimeet.grimeet.common.config.websocket.handler;

import com.grimeet.grimeet.common.exception.GrimeetException;
import com.grimeet.grimeet.common.exception.dto.WebSocketErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Slf4j
@Controller
public class WebSocketExceptionHandler {

  @MessageExceptionHandler(MethodArgumentNotValidException.class)
  @SendToUser("/queue/errors")
  public WebSocketErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
    String errorMessage = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return new WebSocketErrorResponse("VALIDATION_ERROR", errorMessage, 400);
  }

  @MessageExceptionHandler(GrimeetException.class)
  @SendToUser("/queue/errors")
  public WebSocketErrorResponse handleGrimeetException(GrimeetException ex) {
    return new WebSocketErrorResponse("GRIMEET_ERROR", ex.getMessage(), ex.getStatus());
  }

  @MessageExceptionHandler(Exception.class)
  @SendToUser("/queue/errors")
  public WebSocketErrorResponse handleOtherExceptions(Exception ex) {
    log.error("❌ Unknown WebSocket Error", ex);
    return new WebSocketErrorResponse("INTERNAL_ERROR", "알 수 없는 오류가 발생했습니다.", 500);
  }
}
