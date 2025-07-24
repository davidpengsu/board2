package com.example.board.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * JWT 토큰 생성, 검증, 파싱의 단일 책임만을 담당합니다.
 * 
 * JWT 토큰 관련 모든 기능을 제공하는 유틸리티 클래스
 */
@Slf4j
@Component
public class JwtTokenUtil {
    
    private final SecretKey secretKey;
    private final long tokenValidityInMilliseconds;
    
    /**
     * JWT 설정 초기화
     * @param secret JWT 서명에 사용할 비밀키
     * @param tokenValidityInSeconds 토큰 유효시간 (초)
     */
    public JwtTokenUtil(
            @Value("${jwt.secret:mySecretKeyForJwtTokenGenerationThatIsVerySecureAndLong}") String secret,
            @Value("${jwt.token-validity-in-seconds:86400}") long tokenValidityInSeconds) {
        
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }
    
    /**
     * JWT 토큰 생성
     * @param userId 사용자 ID
     * @param username 사용자 이름 
     * @param role 사용자 역할
     * @return 생성된 JWT 토큰
     */
    public String generateToken(String userId, String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + tokenValidityInMilliseconds);
        
        return Jwts.builder()
                .subject(userId)                            // 사용자 ID (토큰의 주체)
                .claim("username", username)                // 사용자 이름
                .claim("role", role)                        // 사용자 역할
                .issuedAt(now)                             // 토큰 발행 시간
                .expiration(expiryDate)                    // 토큰 만료 시간
                .signWith(secretKey, Jwts.SIG.HS512)       // 서명
                .compact();
    }
    
    /**
     * JWT 토큰에서 사용자 ID 추출
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    public String getUserIdFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }
    
    /**
     * JWT 토큰에서 사용자 이름 추출
     * @param token JWT 토큰
     * @return 사용자 이름
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).get("username", String.class);
    }
    
    /**
     * JWT 토큰에서 사용자 역할 추출
     * @param token JWT 토큰
     * @return 사용자 역할
     */
    public String getRoleFromToken(String token) {
        return getClaimsFromToken(token).get("role", String.class);
    }
    
    /**
     * JWT 토큰에서 만료 시간 추출
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimsFromToken(token).getExpiration();
    }
    
    /**
     * JWT 토큰 유효성 검증
     * @param token JWT 토큰
     * @return 토큰 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }
    
    /**
     * JWT 토큰이 만료되었는지 확인
     * @param token JWT 토큰
     * @return 만료 여부
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
    
    /**
     * 토큰 유효시간 반환 (밀리초)
     * @return 토큰 유효시간
     */
    public long getTokenValidityInMilliseconds() {
        return tokenValidityInMilliseconds;
    }
    
    /**
     * JWT 토큰에서 Claims 추출
     * @param token JWT 토큰
     * @return Claims 객체
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}