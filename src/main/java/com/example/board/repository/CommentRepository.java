package com.example.board.repository;

import com.example.board.domain.CommentV0;

import java.util.List;
import java.util.Optional;

/**
 * SOLID 원칙 적용: DIP (Dependency Inversion Principle)
 * Service 계층이 구체적인 MyBatis Mapper에 직접 의존하지 않고,
 * 이 추상화된 Repository 인터페이스에 의존하도록 합니다.
 * 
 * 이를 통해 데이터 접근 기술을 MyBatis에서 JPA, JDBC Template 등으로
 * 변경하더라도 Service 계층 코드는 변경하지 않아도 됩니다.
 */
public interface CommentRepository {
    
    /**
     * 댓글 등록
     * @param comment 등록할 댓글
     */
    void save(CommentV0 comment);
    
    /**
     * 특정 게시글의 댓글 목록 조회
     * @param boardIdx 게시글 ID
     * @return 댓글 목록
     */
    List<CommentV0> findByBoardIdx(Long boardIdx);
    
    /**
     * 모든 댓글 목록 조회 (삭제되지 않은 것만)
     * @return 전체 댓글 목록
     */
    List<CommentV0> findAll();
    
    /**
     * 댓글 ID로 조회
     * @param idx 댓글 ID
     * @return 댓글 (Optional)
     */
    Optional<CommentV0> findById(Long idx);
    
    /**
     * 댓글 수정
     * @param comment 수정할 댓글
     */
    void update(CommentV0 comment);
    
    /**
     * 댓글 삭제 (논리 삭제)
     * @param idx 댓글 ID
     */
    void deleteById(Long idx);
}