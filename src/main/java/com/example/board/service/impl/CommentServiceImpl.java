package com.example.board.service.impl;

import com.example.board.domain.CommentV0;
import com.example.board.repository.CommentRepository;
import com.example.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SOLID 원칙 적용:
 * 
 * SRP (Single Responsibility Principle): 
 * 댓글 관련 비즈니스 로직 처리만을 담당합니다.
 * 
 * OCP (Open-Closed Principle):
 * 인터페이스를 통해 확장에는 열려있고 수정에는 닫혀있습니다.
 * 새로운 기능 추가시 이 클래스를 수정하지 않고 새로운 구현체를 만들 수 있습니다.
 * 
 * DIP (Dependency Inversion Principle):
 * 구체적인 Mapper가 아닌 추상화된 Repository 인터페이스에 의존합니다.
 * 이를 통해 데이터 접근 기술 변경시에도 Service 계층은 영향받지 않습니다.
 */
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    
    private final CommentRepository commentRepository;
    
    /**
     * 댓글 등록
     * 비즈니스 로직: 댓글 데이터 검증 및 저장
     * DIP 적용: Repository 추상화를 통해 데이터 저장
     */
    @Override
    public void createComment(CommentV0 comment) {
        commentRepository.save(comment);
    }
    
    /**
     * 특정 게시글의 댓글 목록 조회
     * 비즈니스 로직: 특정 게시글에 속한 삭제되지 않은 댓글만 조회
     * DIP 적용: Repository 추상화를 통해 데이터 조회
     */
    @Override
    public List<CommentV0> getCommentsByBoardIdx(Long boardIdx) {
        return commentRepository.findByBoardIdx(boardIdx);
    }
    
    /**
     * 전체 댓글 목록 조회
     * 비즈니스 로직: 삭제되지 않은 모든 댓글 조회
     * DIP 적용: Repository 추상화를 통해 데이터 조회
     */
    @Override
    public List<CommentV0> getAllComments() {
        return commentRepository.findAll();
    }
    
    /**
     * 댓글 삭제
     * 비즈니스 로직: 댓글 논리 삭제 처리
     * DIP 적용: Repository 추상화를 통해 데이터 삭제
     */
    @Override
    public void deleteComment(Long idx) {
        commentRepository.deleteById(idx);
    }
    
    /**
     * 댓글 수정 (ISP 적용: CommentWriteService 인터페이스 구현)
     * 향후 확장을 위한 메서드
     * DIP 적용: Repository 추상화를 통해 데이터 수정
     */
    @Override
    public void updateComment(CommentV0 comment) {
        commentRepository.update(comment);
    }
}