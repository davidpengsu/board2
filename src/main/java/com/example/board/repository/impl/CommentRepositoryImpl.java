package com.example.board.repository.impl;

import com.example.board.domain.CommentV0;
import com.example.board.mapper.CommentMapper;
import com.example.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SOLID 원칙 적용: DIP (Dependency Inversion Principle)
 * 
 * 이 클래스는 Repository 인터페이스의 구현체로서,
 * 실제로는 MyBatis Mapper를 사용하지만 상위 계층(Service)은
 * 이 구현 세부사항을 알 필요가 없습니다.
 * 
 * 향후 JPA, JDBC Template 등 다른 데이터 접근 기술로 변경시에도
 * 이 클래스만 교체하면 되고, Service 계층은 변경할 필요가 없습니다.
 */
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepository {
    
    private final CommentMapper commentMapper;
    
    /**
     * 댓글 등록
     */
    @Override
    public void save(CommentV0 comment) {
        commentMapper.insertComment(comment);
    }
    
    /**
     * 특정 게시글의 댓글 목록 조회
     */
    @Override
    public List<CommentV0> findByBoardIdx(Long boardIdx) {
        return commentMapper.selectCommentsByBoardIdx(boardIdx);
    }
    
    /**
     * 모든 댓글 목록 조회 (삭제되지 않은 것만)
     */
    @Override
    public List<CommentV0> findAll() {
        return commentMapper.selectAllComments();
    }
    
    /**
     * 댓글 ID로 조회
     * 실무 원칙: 직접 쿼리로 조회, 전체 조회 후 필터링 금지
     */
    @Override
    public Optional<CommentV0> findById(Long idx) {
        CommentV0 comment = commentMapper.selectCommentById(idx);
        return Optional.ofNullable(comment);
    }
    
    /**
     * 댓글 수정 (향후 구현)
     */
    @Override
    public void update(CommentV0 comment) {
        // 향후 CommentMapper에 updateComment 메서드 추가 후 구현
        throw new UnsupportedOperationException("댓글 수정 기능은 아직 구현되지 않았습니다.");
    }
    
    /**
     * 댓글 삭제 (논리 삭제)
     */
    @Override
    public void deleteById(Long idx) {
        commentMapper.deleteComment(idx);
    }
}