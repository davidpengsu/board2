package com.example.board.service;

import com.example.board.dto.LoginRequest;
import com.example.board.dto.LoginResponse;
import com.example.board.dto.SignupRequest;

/**
 * SOLID 원칙 적용: 
 * 
 * DIP (Dependency Inversion Principle)
 * 고수준 모듈(Controller)이 저수준 모듈(Service 구현체)에 의존하지 않도록
 * 추상화(인터페이스)에 의존하게 합니다.
 * 
 * ISP (Interface Segregation Principle)
 * 인증 관련 기능만을 정의하여 인터페이스를 분리합니다.
 */
public interface AuthService {
    
    /**
     * 사용자 회원가입 처리
     * @param signupRequest 회원가입 요청 정보
     */
    void signup(SignupRequest signupRequest);
    
    /**
     * 사용자 로그인 처리
     * @param loginRequest 로그인 요청 정보
     * @return 로그인 응답 (JWT 토큰 포함)
     */
    LoginResponse login(LoginRequest loginRequest);
    
    /**
     * JWT 토큰 검증
     * @param token JWT 토큰
     * @return 토큰 유효성 여부
     */
    boolean validateToken(String token);
    
    /**
     * JWT 토큰에서 사용자 ID 추출
     * @param token JWT 토큰
     * @return 사용자 ID
     */
    String getUserIdFromToken(String token);
}