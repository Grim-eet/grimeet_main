package com.grimeet.grimeet.domain.socialAccount.controller;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.domain.socialAccount.dto.SocialAccountRequestDto;
import com.grimeet.grimeet.domain.socialAccount.dto.SocialAccountResponseDto;
import com.grimeet.grimeet.domain.socialAccount.service.SocialAccountFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/social-account")
public class SocialAccountController {

  private final SocialAccountFacade socialAccountFacade;

  @Operation(summary = "소셜 계정 연결", description = "사용자가 소셜 계정을 연결합니다.")
  @ApiResponses(
          value = {
                  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "소셜 계정 연결 성공"),
                  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 요청"),
                  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "인증 실패"),
                  @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "사용자 또는 소셜 계정 정보 없음")
          }
  )
  @PostMapping()
  public ResponseEntity<SocialAccountResponseDto> linkeSocialAccount(
          @AuthenticationPrincipal UserPrincipalDetails principal,
          @Valid @RequestBody SocialAccountRequestDto socialAccountRequestDto
          ) {
    String username = principal.getUsername();
    SocialAccountResponseDto socialAccountResponseDto = socialAccountFacade.linkSocialAccount(
            username,
            socialAccountRequestDto
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(socialAccountResponseDto);
  }
}
