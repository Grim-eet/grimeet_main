package com.grimeet.grimeet.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {

  // AUTH

  // JWT
  UN_AUTHENTICATION_TOKEN(HttpStatus.UNAUTHORIZED, 401, "토큰 정보가 없습니다."),
  INVALID_TOKEN(HttpStatus.FORBIDDEN, 403, "유효하지 않은 토큰입니다."),

  // USER
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "일치하는 유저정보를 찾을 수 없습니다."),
  INVALID_USER_LOGIN_INFO(HttpStatus.BAD_REQUEST, 400, "아이디 혹은 비밀번호가 일치하지 않습니다."),
  INVALID_ROLE(HttpStatus.FORBIDDEN, 403, "접근할 수 없습니다."),
  INVALID_USER_STATUS(HttpStatus.BAD_REQUEST, 400, "잘못된 유저상태입니다.");


  private final int status;
  private final int customCode;
  private final String message;
  private final String errorStatus;

  ExceptionStatus(HttpStatus httpStatus, int customCode, String message) {
    this.status = httpStatus.value();
    this.customCode = customCode;
    this.message = message;
    this.errorStatus = httpStatus.getReasonPhrase();
  }
}
