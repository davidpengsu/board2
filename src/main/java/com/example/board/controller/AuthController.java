package com.example.board.controller;

import com.example.board.dto.ApiResponse;
import com.example.board.dto.LoginRequest;
import com.example.board.dto.LoginResponse;
import com.example.board.dto.SignupRequest;
import com.example.board.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

/**
 * SOLID 원칙 적용:
 * 
 * SRP (Single Responsibility Principle): 
 * 인증 관련 HTTP 요청/응답 처리만을 담당합니다.
 * 
 * DIP (Dependency Inversion Principle):
 * 구체적인 구현체가 아닌 인터페이스(AuthService)에 의존합니다.
 * 
 * OCP (Open-Closed Principle):
 * Service 인터페이스를 통해 확장에는 열려있고 수정에는 닫혀있습니다.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 회원가입 - POST /auth/signup
     * 책임: HTTP 요청 처리 및 응답 반환
     */
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        
        ApiResponse<Void> response = ApiResponse.success("회원가입이 성공적으로 완료되었습니다.");
        
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자 로그인 - POST /auth/login
     * 책임: HTTP 요청 처리 및 응답 반환
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.login(loginRequest);
        
        ApiResponse<LoginResponse> response = ApiResponse.success(
            "로그인이 성공적으로 완료되었습니다.", 
            loginResponse
        );
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * 토큰 검증 - GET /auth/validate
     * 개발/테스트 목적으로 토큰 유효성을 확인하는 엔드포인트
     */
    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<String>> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            ApiResponse<String> response = ApiResponse.failure("토큰이 없거나 형식이 올바르지 않습니다.");
            return ResponseEntity.badRequest().body(response);
        }
        
        String token = authHeader.substring(7); // "Bearer " 제거
        boolean isValid = authService.validateToken(token);
        
        if (isValid) {
            String userId = authService.getUserIdFromToken(token);
            ApiResponse<String> response = ApiResponse.success(
                "유효한 토큰입니다.", 
                "인증된 사용자: " + userId
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response = ApiResponse.failure("유효하지 않은 토큰입니다.");
            return ResponseEntity.badRequest().body(response);
        }
    }
}