package com.grimeet.grimeet.common.jwt;

import com.grimeet.grimeet.common.config.oauth.UserPrincipalDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expirationMs}")
  private long expirationMs;

  @Value("${jwt.refreshExpirationMs}")
  private long refreshExpirationMs;

  @Value("${jwt.token-prefix}")
  private String tokenPrefix;

  @Value("${jwt.header}")
  private String header;

  private SecretKey key;

  /**
   * SecretKey를 지연 초기화하여 반환한다.
   * @return SecretKey
   */
  private SecretKey getKey() {
    if (key == null) {
      key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
    return key;
  }

  /**
   * AccessToken 생성
   * Header, Payload, Signature로 구성된 JWT Token을 생성한다.
   * Header에는 알고리즘과 토큰 타입을 설정한다.
   * Payload에는 사용자 정보를 설정한다.
   * Signature에는 SecretKey로 서명한다.
   * Algorithm은 HS512를 사용한다. -> HS512채택 이유는 HS256보다 강력한 보안을 제공하지만 성능차이는 미미하다.(추후 테스트 필요)
   * @param userDetails
   * @return JWT Token
   */
  public String generateAccessToken(UserPrincipalDetails userDetails) {
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + expirationMs);

    return Jwts.builder()
      .setSubject(userDetails.getUsername())
      .claim("email", userDetails.getUsername())
      .claim("role", userDetails.getAuthorities())
      .setIssuedAt(now)
      .setExpiration(expireDate)
      .signWith(getKey(), SignatureAlgorithm.HS512)
      .compact();
  }

  /**
   * RefreshToken 생성
   * @param userDetails
   * @return JWT Token
   */
  public String generateRefreshToken(UserPrincipalDetails userDetails) {
    Date now = new Date();
    Date expireDate = new Date(now.getTime() + refreshExpirationMs);

    return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .claim("email", userDetails.getUsername())
            .claim("role", userDetails.getAuthorities())
            .setIssuedAt(now)
            .setExpiration(expireDate)
            .signWith(getKey(), SignatureAlgorithm.HS512)
            .compact();
  }

  // AccessToken에서 사용자 이름 추출
  public String getUsernameFromAccessToken(String token) {
    return extractUsername(token);
  }

  // RefreshToken에서 사용자 이름 추출
  public String getUsernameFromRefreshToken(String token) {
    return extractUsername(token);
  }

  /**
   * JWT Token에서 사용자 정보 추출 -> 사용자 이름(Subject)을 추출한다.(AccessToken, RefreshToken 공통)
   * @param token
   * @return 사용자 이름
   */
  public String extractUsername(String token) {
    return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  // AccessToken 유효성 검사
  public boolean validateAccessToken(String token) {
    return validateToken(token);
  }

  // RefreshToken 유효성 검사
  public boolean validateRefreshToken(String token) {
    if(token == null || token.isEmpty()) return false;
    return validateToken(token);
  }

  /**
   * JWT Token 유효성 검사 -> 서명이 올바른지, 만료되지 않았는지 확인(AccessToken, RefreshToken 공통)
   * @param token
   * @return 유효성 여부
   */
  private boolean validateToken(String token){
    try {
      Jwts.parserBuilder()
              .setSigningKey(getKey())
              .build()
              .parseClaimsJws(token);
      return true;
    } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
      System.out.println("Invalid JWT signature");
    } catch (ExpiredJwtException e) {
      System.out.println("Expired JWT token");
    } catch (UnsupportedJwtException e) {
      System.out.println("Unsupported JWT token");
    } catch (IllegalArgumentException e) {
      System.out.println("JWT claims string is empty");
    }
    return false;
  }

  // Request Header에서 JWT Token 추출
  public String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader(header);
    if(bearerToken != null && bearerToken.startsWith(tokenPrefix + " ")) {
      return bearerToken.substring(tokenPrefix.length() + 1);
    }
    return null;
  }
}
