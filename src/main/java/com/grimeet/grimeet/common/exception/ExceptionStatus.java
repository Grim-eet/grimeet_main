package com.grimeet.grimeet.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionStatus {

  // AUTH
  INVALID_USER_LOGIN_INFO(HttpStatus.BAD_REQUEST, 400, "아이디 혹은 비밀번호가 일치하지 않습니다."),
  INVALID_USER_EMAIL_AUTH(HttpStatus.BAD_REQUEST, 400, "인증되지 않은 이메일입니다."),

  // JWT
  UN_AUTHENTICATION_TOKEN(HttpStatus.UNAUTHORIZED, 401, "토큰 정보가 없습니다."),
  INVALID_TOKEN(HttpStatus.FORBIDDEN, 403, "유효하지 않은 토큰입니다."),

  // USER
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "일치하는 유저정보를 찾을 수 없습니다."),
  EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 400, "이미 존재하는 이메일입니다."),
  NICKNAME_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 400, "이미 존재하는 닉네임입니다."),
  PHONE_NUMBER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, 400, "이미 존재하는 전화번호입니다."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, 400, "비밀번호가 유효하지 않습니다."),
  INVALID_ROLE(HttpStatus.FORBIDDEN, 403, "접근할 수 없습니다."),
  INVALID_USER_STATUS(HttpStatus.BAD_REQUEST, 400, "잘못된 유저상태입니다."),

  //UPLOAD
  INVALID_FILE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 이미지입니다."),
  S3_UPLOAD_FAIL(HttpStatus.BAD_REQUEST, 400, "S3 이미지 업로드에 실패했습니다."),
  S3_DELETE_FAIL(HttpStatus.BAD_REQUEST, 400, "S3 이미지 삭제에 실패했습니다."),

  // FOLLOW
  INVALID_FOLLOW(HttpStatus.BAD_REQUEST, 400, "잘못된 팔로우 요청입니다.");

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
