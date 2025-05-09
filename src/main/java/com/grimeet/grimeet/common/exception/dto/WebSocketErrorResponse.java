package com.grimeet.grimeet.common.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WebSocketErrorResponse {
  private String errorType;
  private String message;
  private int status;
}
