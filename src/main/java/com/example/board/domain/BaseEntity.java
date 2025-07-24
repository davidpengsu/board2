package com.example.board.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * SOLID 원칙 적용: LSP (Liskov Substitution Principle)
 * 모든 엔티티가 공통으로 가져야 할 속성을 정의합니다.
 * 하위 클래스들은 이 클래스를 대체할 수 있어야 합니다.
 * 
 * 공통 속성:
 * - idx: 고유 식별자
 * - regDate: 등록일시  
 * - delYn: 삭제 여부
 */
@Data
public abstract class BaseEntity {
    
    /**
     * 엔티티 고유 식별자
     * 조회 시에만 사용되는 필드 (등록 시에는 DB에서 자동 생성)
     */
    protected Long idx;
    
    /**
     * 등록일시
     * 조회 시에만 사용되는 필드 (등록 시에는 DB에서 NOW()로 자동 생성)
     */
    protected LocalDateTime regDate;
    
    /**
     * 삭제 여부
     * 'Y': 삭제됨, 'N': 삭제되지 않음 (기본값)
     */
    protected String delYn;
    
    /**
     * LSP 적용: 모든 하위 클래스에서 동일하게 동작해야 하는 메서드
     * 엔티티가 삭제되었는지 확인
     */
    public boolean isDeleted() {
        return "Y".equals(this.delYn);
    }
    
    /**
     * LSP 적용: 모든 하위 클래스에서 동일하게 동작해야 하는 메서드  
     * 엔티티가 유효한지 확인 (삭제되지 않았고 ID가 존재)
     */
    public boolean isValid() {
        return this.idx != null && !isDeleted();
    }
}