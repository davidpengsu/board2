package com.example.board.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * SOLID 원칙 적용: LSP (Liskov Substitution Principle)
 * BaseEntity의 하위 클래스로서, BaseEntity를 완전히 대체할 수 있습니다.
 * BaseEntity의 모든 메서드(isDeleted, isValid)가 정상적으로 동작합니다.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentV0 extends BaseEntity {
    
    // 등록 시 필수 입력 필드
    @NotNull(message = "게시글 번호는 필수입니다.")
    private Long boardIdx;              // 게시글 번호 (외래키)
    
    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    @Size(max = 500, message = "댓글은 500자를 초과할 수 없습니다.")
    private String comment;             // 댓글 내용
    
    @Size(max = 50, message = "작성자명은 50자를 초과할 수 없습니다.")
    private String writerName;
    
    /**
     * 작성자 ID (로그인한 사용자의 ID)
     * 인증된 사용자만 댓글을 작성할 수 있으므로 필수값
     */
    private String writerId;          // 댓글 작성자명
    
    /**
     * LSP 적용: 댓글이 표시 가능한지 확인
     * 기본적으로는 유효하고, 추가로 댓글 내용과 게시글 참조가 있어야 함
     */
    public boolean isDisplayable() {
        return isValid() && comment != null && !comment.trim().isEmpty() 
               && boardIdx != null && boardIdx > 0;
    }
    
    /**
     * LSP 적용: 댓글이 특정 게시글에 속하는지 확인
     */
    public boolean belongsToBoard(Long boardId) {
        return boardId != null && boardId.equals(this.boardIdx);
    }
}