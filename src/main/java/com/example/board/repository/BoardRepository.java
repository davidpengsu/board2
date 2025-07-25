package com.example.board.repository;

import com.example.board.domain.BoardV0;

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
public interface BoardRepository {
    
    /**
     * 게시글 등록
     * @param board 등록할 게시글
     */
    void save(BoardV0 board);
    
    /**
     * 게시글 목록 조회 (삭제되지 않은 것만)
     * @return 게시글 목록
     */
    List<BoardV0> findAll();
    
    /**
     * 게시글 ID로 조회
     * @param idx 게시글 ID
     * @return 게시글 (Optional)
     */
    Optional<BoardV0> findById(Long idx);
    
    /**
     * 게시글 수정
     * @param board 수정할 게시글
     */
    void update(BoardV0 board);
    
    /**
     * 게시글 삭제 (논리 삭제)
     * @param idx 게시글 ID
     */
    void deleteById(Long idx);
    
    /**
     * 게시글 존재 여부 확인 (효율적인 COUNT 쿼리)
     * @param idx 게시글 ID
     * @return 존재하면 true, 아니면 false
     */
    boolean existsById(Long idx);
}