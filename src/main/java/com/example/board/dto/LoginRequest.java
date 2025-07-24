package com.example.board.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * 로그인 요청 데이터만을 담당하는 클래스입니다.
 */
@Data
public class LoginRequest {
    
    /**
     * 로그인 사용자 ID
     */
    @NotBlank(message = "사용자 ID는 필수 입력값입니다.")
    private String userId;
    
    /**
     * 로그인 비밀번호
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
}