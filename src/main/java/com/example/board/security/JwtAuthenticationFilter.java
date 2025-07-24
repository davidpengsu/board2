package com.example.board.security;

import com.example.board.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * JWT 토큰 기반 인증 처리만을 담당하는 필터입니다.
 * 
 * Spring Security 필터 체인에서 JWT 토큰을 검증하고
 * 인증된 사용자 정보를 SecurityContext에 설정합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 모든 HTTP 요청에 대해 JWT 토큰 검증 수행
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        try {
            // Authorization 헤더에서 JWT 토큰 추출
            String jwt = getJwtFromRequest(request);
            
            // 토큰이 존재하고 유효한 경우 인증 정보 설정
            if (StringUtils.hasText(jwt) && jwtTokenUtil.validateToken(jwt)) {
                String userId = jwtTokenUtil.getUserIdFromToken(jwt);
                String role = jwtTokenUtil.getRoleFromToken(jwt);
                
                // Spring Security 인증 객체 생성
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(
                        userId, 
                        null, 
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                    );
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                
                // SecurityContext에 인증 정보 설정
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
                log.debug("JWT 토큰으로 인증된 사용자: {}, 역할: {}", userId, role);
            }
        } catch (Exception ex) {
            log.error("JWT 토큰 인증 처리 중 오류 발생: {}", ex.getMessage());
            // 인증 실패시 SecurityContext 초기화
            SecurityContextHolder.clearContext();
        }
        
        // 다음 필터로 요청 전달
        filterChain.doFilter(request, response);
    }
    
    /**
     * HTTP 요청에서 JWT 토큰 추출
     * Authorization 헤더에서 "Bearer " 접두사를 제거하고 토큰 반환
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // "Bearer " 제거
        }
        
        return null;
    }
}