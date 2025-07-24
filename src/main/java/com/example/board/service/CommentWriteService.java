package com.example.board.service;

import com.example.board.domain.CommentV0;

/**
 * SOLID 원칙 적용: ISP (Interface Segregation Principle)
 * 댓글 쓰기 관련 기능만을 정의한 인터페이스입니다.
 * 클라이언트가 사용하지 않는 메서드에 의존하지 않도록 인터페이스를 분리했습니다.
 */
public interface CommentWriteService {
    
    /**
     * 댓글 등록
     * @param comment 등록할 댓글 정보
     */
    void createComment(CommentV0 comment);
    
    /**
     * 댓글 수정 (향후 확장을 위한 메서드)
     * @param comment 수정할 댓글 정보
     */
    void updateComment(CommentV0 comment);
    
    /**
     * 댓글 삭제
     * @param idx 댓글 번호
     */
    void deleteComment(Long idx);
    
    /**
     * 댓글 작성자 권한 확인
     * SOLID 원칙 적용: SRP - 권한 검증의 단일 책임
     * @param commentIdx 댓글 번호
     * @param userId 사용자 ID
     * @return 작성자가 맞으면 true, 아니면 false
     */
    boolean isOwner(Long commentIdx, String userId);
}