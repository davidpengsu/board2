package com.example.board.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * SOLID 원칙 적용: LSP (Liskov Substitution Principle)
 * BaseEntity의 하위 클래스로서, BaseEntity를 완전히 대체할 수 있습니다.
 * BaseEntity의 모든 메서드(isDeleted, isValid)가 정상적으로 동작합니다.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BoardV0 extends BaseEntity {
    
    // 계산된 필드들 (SELECT 시에만 계산되는 값들)
    private Integer commentCount;       // 댓글 개수 (LEFT JOIN + COUNT)
    private Integer isNew;              // 신규 게시글 여부 (1: 오늘 작성, 0: 이전 작성)
    
    // 등록/수정 시 필수 입력 필드
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
    private String title;
    
    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Size(max = 2000, message = "내용은 2000자를 초과할 수 없습니다.")
    private String content;
    
    @Size(max = 50, message = "작성자명은 50자를 초과할 수 없습니다.")
    private String writerNm;
    
    /**
     * 작성자 ID (로그인한 사용자의 ID)
     * 인증된 사용자만 게시글을 작성할 수 있으므로 필수값
     */
    private String writerId;
    
    /**
     * LSP 적용: 부모 클래스의 동작을 확장하되 기본 동작은 변경하지 않습니다.
     * 게시글이 신규인지 확인하는 비즈니스 로직을 추가
     */
    public boolean isNewBoard() {
        return this.isNew != null && this.isNew == 1;
    }
    
    /**
     * LSP 적용: 게시글이 표시 가능한지 확인
     * 기본적으로는 유효하고, 추가로 제목과 내용이 있어야 함
     */
    public boolean isDisplayable() {
        return isValid() && title != null && !title.trim().isEmpty() 
               && content != null && !content.trim().isEmpty();
    }
}

