package com.grimeet.grimeet.domain.socialAccount.controller;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import com.grimeet.grimeet.domain.auth.service.GoogleOAuthServiceImpl;
import com.grimeet.grimeet.domain.auth.service.KakaoOAuthServiceImpl;
import com.grimeet.grimeet.domain.auth.service.NaverOAuthServiceImpl;
import com.grimeet.grimeet.domain.socialAccount.service.SocialAccountFacade;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/social-account")
public class SocialAccountController {

  private final SocialAccountFacade socialAccountFacade;
  private final GoogleOAuthServiceImpl googleOAuthService;
  private final KakaoOAuthServiceImpl kakaoOAuthService;
  private final NaverOAuthServiceImpl naverOAuthService;

//  @Operation(summary = "소셜 계정 연결", description = "사용자가 소셜 계정을 연결합니다.")
//  @ApiResponses(
//          value = {
//                  @ApiResponse(responseCode = "201", description = "소셜 계정 연결 성공"),
//                  @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//                  @ApiResponse(responseCode = "401", description = "인증 실패"),
//                  @ApiResponse(responseCode = "404", description = "사용자 또는 소셜 계정 정보 없음")
//          }
//  )
//  @PostMapping()
//  public ResponseEntity<SocialAccountResponseDto> linkSocialAccount(
//          @AuthenticationPrincipal UserPrincipalDetails principal,
//          @Valid @RequestBody SocialAccountRequestDto socialAccountRequestDto
//          ) {
//    String username = principal.getUsername();
//    SocialAccountResponseDto socialAccountResponseDto = socialAccountFacade.linkSocialAccount(
//            username,
//            socialAccountRequestDto
//    );
//
//    return ResponseEntity.status(HttpStatus.CREATED).body(socialAccountResponseDto);
//  }
//
//  @Operation(summary = "구글 소셜 연동 인가 URL 생성", description = "구글 소셜 연동을 위한 인가 URL을 반환합니다.")
//  @ApiResponses(value = {
//          @ApiResponse(responseCode = "200", description = "구글 인가 URL 반환 성공"),
//          @ApiResponse(responseCode = "500", description = "서버 오류")
//  })
//  @GetMapping("/connect/google")
//  public Map<String, String> getGoogleConnectUrl() {
//    String googleConnectUrl = googleAuthService.generateGoogleOAuth2Url();
//    return Map.of("url", googleConnectUrl);
//  }
//
//  @Operation(summary = "구글 소셜 계정 연동", description = "로그인한 사용자가 구글 계정을 연동합니다.")
//  @ApiResponses(value = {
//          @ApiResponse(responseCode = "201", description = "구글 소셜 계정 연동 성공"),
//          @ApiResponse(responseCode = "400", description = "잘못된 요청"),
//          @ApiResponse(responseCode = "401", description = "인증 실패"),
//          @ApiResponse(responseCode = "409", description = "이미 연동된 소셜 계정"),
//  })
//  @PostMapping("/google")
//  public void linkGoogleAccount(
//          @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipalDetails principal,
//          @RequestParam("code") String code
//  ) {
//    String username = principal.getUsername();
//    googleAuthService.linkGoogleAccount(username, code);
//  }

  @GetMapping("/connect/google")
  public Map<String, String> getGoogleConnectUrl(
          @AuthenticationPrincipal UserPrincipalDetails userPrincipal
  ) {
    String googleConnectUrl = googleOAuthService.generateAuthUrl(userPrincipal.getUsername());
    return Map.of("url", googleConnectUrl);
  }

  @PostMapping("/google")
  public void linkGoogleAccount(
          @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipalDetails userPrincipal,
          @RequestParam("code") String code,
          @RequestParam("state") String state
  ) {
    String username = userPrincipal.getUsername();
    googleOAuthService.linkAccount(username, code, state);
  }

  @GetMapping("/connect/kakao")
  public Map<String, String> getKakaoConnectUrl(
          @AuthenticationPrincipal UserPrincipalDetails userPrincipal
  ) {
    String kakaoConnectUrl = kakaoOAuthService.generateAuthUrl(userPrincipal.getUsername());
    return Map.of("url", kakaoConnectUrl);
  }

  @PostMapping("/kakao")
  public void linkKakaoAccount(
          @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipalDetails userPrincipal,
          @RequestParam("code") String code,
          @RequestParam("state") String state
  ) {
    String username = userPrincipal.getUsername();
    kakaoOAuthService.linkAccount(username, code, state);
  }

  @GetMapping("/connect/naver")
  public Map<String, String> getNaverConnectUrl(
          @AuthenticationPrincipal UserPrincipalDetails userPrincipal
  ) {
    String naverConnectUrl = naverOAuthService.generateAuthUrl(userPrincipal.getUsername());
    return Map.of("url", naverConnectUrl);
  }

  @PostMapping("/naver")
  public void linkNaverAccount(
          @Parameter(hidden = true) @AuthenticationPrincipal UserPrincipalDetails userPrincipal,
          @RequestParam("code") String code,
          @RequestParam("state") String state
  ) {
    String username = userPrincipal.getUsername();
    naverOAuthService.linkAccount(username, code, state);
  }

}
