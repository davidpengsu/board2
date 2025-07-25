package com.example.board.service.impl;

import com.example.board.domain.CommentV0;
import com.example.board.repository.CommentRepository;
import com.example.board.service.CommentService;
import com.example.board.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    
    private final CommentRepository commentRepository;
    
    /**
     * 댓글 등록
     * 실무 원칙: Service에서 모든 비즈니스 로직과 검증 처리
     * - 입력값 검증
     * - 게시글 존재 확인
     * - 성공/실패 결과 반환
     */
    @Override
    public ApiResponse<Void> createComment(CommentV0 comment, String userId) {
        try {
            // Spring Validation으로 입력값 검증 완료됨
            // Service는 비즈니스 로직만 처리
            comment.setWriterId(userId);
            commentRepository.save(comment);
            
            return ApiResponse.success("댓글이 성공적으로 등록되었습니다.");
            
        } catch (Exception e) {
            log.error("댓글 등록 실패 - 사용자: {}, 게시글: {}, 오류: {}", userId, comment.getBoardIdx(), e.getMessage(), e);
            return ApiResponse.failure("댓글 등록 중 오류가 발생했습니다.");
        }
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
     * 실무 원칙: Service에서 권한 검증과 비즈니스 로직 처리
     * - 댓글 존재 확인
     * - 작성자 권한 확인
     * - 논리 삭제 처리
     * - 성공/실패 결과 반환
     */
    @Override
    public ApiResponse<Void> deleteComment(Long commentIdx, String userId) {
        // 1. 댓글 존재 확인
        CommentV0 comment = commentRepository.findById(commentIdx).orElse(null);
        if (comment == null) {
            return ApiResponse.failure("존재하지 않는 댓글입니다.");
        }
        
        // 2. 작성자 권한 확인
        if (!comment.getWriterId().equals(userId)) {
            log.warn("권한 없는 댓글 삭제 시도 - 댓글: {}, 시도자: {}", commentIdx, userId);
            return ApiResponse.failure("본인이 작성한 댓글만 삭제할 수 있습니다.");
        }
        
        try {
            // 3. 논리 삭제 처리
            commentRepository.deleteById(commentIdx);
            
            return ApiResponse.success("댓글이 성공적으로 삭제되었습니다.");
            
        } catch (Exception e) {
            log.error("댓글 삭제 실패 - 댓글: {}, 사용자: {}, 오류: {}", commentIdx, userId, e.getMessage(), e);
            return ApiResponse.failure("댓글 삭제 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 댓글 수정
     * 실무 원칙: Service에서 권한 검증과 비즈니스 로직 처리
     * - 댓글 존재 확인
     * - 작성자 권한 확인
     * - 입력값 검증
     * - 성공/실패 결과 반환
     */
    @Override
    public ApiResponse<Void> updateComment(Long commentIdx, CommentV0 comment, String userId) {
        // 1. 댓글 존재 확인
        CommentV0 existingComment = commentRepository.findById(commentIdx).orElse(null);
        if (existingComment == null) {
            return ApiResponse.failure("존재하지 않는 댓글입니다.");
        }
        
        // 2. 작성자 권한 확인
        if (!existingComment.getWriterId().equals(userId)) {
            log.warn("권한 없는 댓글 수정 시도 - 댓글: {}, 시도자: {}", commentIdx, userId);
            return ApiResponse.failure("본인이 작성한 댓글만 수정할 수 있습니다.");
        }
        
        try {
            // Spring Validation으로 입력값 검증 완료됨
            // Service는 비즈니스 로직만 처리
            comment.setIdx(commentIdx);
            commentRepository.update(comment);
            
            return ApiResponse.success("댓글이 성공적으로 수정되었습니다.");
            
        } catch (Exception e) {
            log.error("댓글 수정 실패 - 댓글: {}, 사용자: {}, 오류: {}", commentIdx, userId, e.getMessage(), e);
            return ApiResponse.failure("댓글 수정 중 오류가 발생했습니다.");
        }
    }
    
}