package com.grimeet.grimeet.common.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GrimeetException extends RuntimeException {

  private final ExceptionStatus exceptionStatus;

  public ExceptionStatus getExceptionInfo() {
    return exceptionStatus;
  }

  @Override
  public String getMessage() {
    return exceptionStatus.getMessage();
  }

  public int getStatus() {
    return exceptionStatus.getStatus();
  }
}
