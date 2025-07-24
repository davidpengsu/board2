package com.example.board.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SOLID 원칙 적용: LSP (Liskov Substitution Principle)
 * BaseEntity의 하위 클래스로서, BaseEntity를 완전히 대체할 수 있습니다.
 * 
 * 사용자 엔티티 - 로그인 및 인증을 위한 간소화된 도메인 모델
 * 회원가입 기능 없이 DB에 미리 저장된 사용자 정보로 로그인 처리
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    /**
     * 사용자 로그인 ID (고유값)
     */
    private String userId;
    
    /**
     * 사용자 비밀번호 (암호화되어 저장)
     */
    private String password;
    
    /**
     * 사용자 이름
     */
    private String username;
    
    /**
     * 이메일 주소
     */
    private String email;
    
    /**
     * 사용자 역할 (USER, ADMIN 등)
     * 기본값: USER
     */
    private String role = "USER";
    
    /**
     * 계정 활성화 여부
     * 'Y': 활성화, 'N': 비활성화 (기본값: Y)
     */
    private String activeYn = "Y";
    
    /**
     * LSP 적용: 사용자 계정이 활성화되어 있는지 확인
     */
    public boolean isActive() {
        return "Y".equals(this.activeYn) && isValid();
    }
    
    /**
     * LSP 적용: 사용자가 관리자인지 확인
     */
    public boolean isAdmin() {
        return "ADMIN".equals(this.role);
    }
    
    /**
     * LSP 적용: 로그인 가능한 상태인지 확인
     * 계정이 활성화되어 있고, 삭제되지 않은 상태여야 함
     */
    public boolean canLogin() {
        return isActive() && !isDeleted();
    }
}