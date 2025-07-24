package com.example.board.config;

import com.example.board.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * Spring Security 설정만을 담당하는 설정 클래스입니다.
 * 
 * JWT 기반 인증을 위한 Spring Security 설정
 * - 세션 사용하지 않음 (Stateless)
 * - JWT 토큰 기반 인증
 * - 특정 엔드포인트는 인증 없이 접근 가능
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
     * BCrypt 알고리즘 사용
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Spring Security 필터 체인 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CSRF 보호 비활성화 (JWT 사용시 불필요)
            .csrf(csrf -> csrf.disable())
            
            // 세션 관리 정책: STATELESS (세션 사용하지 않음)
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            
            // 요청 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 인증 없이 접근 가능한 엔드포인트
                .requestMatchers("/auth/**").permitAll()           // 로그인, 회원가입 등
                .requestMatchers("/error").permitAll()             // 에러 페이지
                
                // 게시글 조회는 인증 없이 접근 가능
                .requestMatchers(HttpMethod.GET, "/board").permitAll()      // 게시글 목록 조회
                .requestMatchers(HttpMethod.GET, "/board/**").permitAll()   // 게시글 상세 조회
                
                // 댓글 조회는 인증 없이 접근 가능  
                .requestMatchers(HttpMethod.GET, "/comment").permitAll()    // 전체 댓글 조회
                .requestMatchers(HttpMethod.GET, "/comment/**").permitAll() // 특정 게시글 댓글 조회
                
                // 나머지 요청 (POST, PUT, DELETE 등)은 인증 필요
                .anyRequest().authenticated()
            )
            
            // JWT 인증 필터를 UsernamePasswordAuthenticationFilter 전에 추가
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}