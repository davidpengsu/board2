package com.example.board.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * 로그인 응답 데이터만을 담당하는 클래스입니다.
 */
@Getter
@Builder
public class LoginResponse {
    
    /**
     * JWT 액세스 토큰
     */
    private final String accessToken;
    
    /**
     * 토큰 타입 (Bearer)
     */
    private final String tokenType;
    
    /**
     * 토큰 만료 시간 (초 단위)
     */
    private final Long expiresIn;
    
    /**
     * 사용자 정보
     */
    private final UserInfo userInfo;
    
    /**
     * 사용자 기본 정보 내부 클래스
     */
    @Getter
    @Builder
    public static class UserInfo {
        private final String userId;
        private final String username;
        private final String email;
        private final String role;
    }
    
    /**
     * LoginResponse 생성을 위한 팩토리 메서드
     */
    public static LoginResponse of(String accessToken, Long expiresIn, 
                                   String userId, String username, String email, String role) {
        UserInfo userInfo = UserInfo.builder()
                .userId(userId)
                .username(username)
                .email(email)
                .role(role)
                .build();
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .userInfo(userInfo)
                .build();
    }
}