package com.example.board.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * 회원가입 요청 데이터만을 담당하는 클래스입니다.
 */
@Data
public class SignupRequest {
    
    /**
     * 사용자 ID
     */
    @NotBlank(message = "사용자 ID는 필수 입력값입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]{4,20}$", message = "사용자 ID는 영문, 숫자 조합 4-20자여야 합니다.")
    private String userId;
    
    /**
     * 비밀번호
     */
    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 6, message = "비밀번호는 최소 6자 이상이어야 합니다.")
    private String password;
    
    /**
     * 사용자 이름
     */
    @NotBlank(message = "사용자 이름은 필수 입력값입니다.")
    @Size(max = 50, message = "사용자 이름은 50자를 초과할 수 없습니다.")
    private String username;
    
    /**
     * 이메일
     */
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "올바른 이메일 형식이 아닙니다.")
    private String email;
}