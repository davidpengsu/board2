package com.example.board.service.impl;

import com.example.board.domain.User;
import com.example.board.dto.LoginRequest;
import com.example.board.dto.LoginResponse;
import com.example.board.dto.SignupRequest;
import com.example.board.repository.UserRepository;
import com.example.board.service.AuthService;
import com.example.board.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * SOLID 원칙 적용:
 * 
 * SRP (Single Responsibility Principle): 
 * 사용자 인증 관련 비즈니스 로직 처리만을 담당합니다.
 * 
 * OCP (Open-Closed Principle):
 * 인터페이스를 통해 확장에는 열려있고 수정에는 닫혀있습니다.
 * 
 * DIP (Dependency Inversion Principle):
 * 구체적인 구현체가 아닌 추상화된 Repository 인터페이스에 의존합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 사용자 회원가입 처리
     * 비즈니스 로직: 사용자 정보 검증 및 저장
     */
    @Override
    public void signup(SignupRequest signupRequest) {
        // 중복 사용자 ID 확인
        if (userRepository.findByUserId(signupRequest.getUserId()).isPresent()) {
            throw new RuntimeException("이미 존재하는 사용자 ID입니다.");
        }
        
        // 새 사용자 생성
        User user = new User();
        user.setUserId(signupRequest.getUserId());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword())); // 비밀번호 암호화
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setRole("USER");
        user.setActiveYn("Y");
        
        userRepository.save(user);
        
        log.info("새 사용자 회원가입 완료: {}", user.getUserId());
    }
    
    /**
     * 사용자 로그인 처리
     * 비즈니스 로직: 사용자 인증 및 JWT 토큰 생성
     */
    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // 사용자 조회
        User user = userRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
        
        // 계정 상태 확인 (간단하게 변경)
        if (!"Y".equals(user.getActiveYn())) {
            throw new RuntimeException("비활성화된 계정입니다.");
        }
        
        // 비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }
        
        // JWT 토큰 생성
        String accessToken = jwtTokenUtil.generateToken(
                user.getUserId(), 
                user.getUsername(), 
                user.getRole()
        );
        
        // 토큰 만료 시간 (초 단위)
        Long expiresIn = jwtTokenUtil.getTokenValidityInMilliseconds() / 1000;
        
        log.info("사용자 로그인 성공: {}", user.getUserId());
        
        return LoginResponse.of(
                accessToken, 
                expiresIn,
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }
    
    /**
     * JWT 토큰 검증
     * 비즈니스 로직: 토큰 유효성 확인
     */
    @Override
    public boolean validateToken(String token) {
        return jwtTokenUtil.validateToken(token);
    }
    
    /**
     * JWT 토큰에서 사용자 ID 추출
     * 비즈니스 로직: 토큰에서 사용자 정보 추출
     */
    @Override
    public String getUserIdFromToken(String token) {
        return jwtTokenUtil.getUserIdFromToken(token);
    }
}