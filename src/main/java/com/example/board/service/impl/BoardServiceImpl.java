package com.example.board.service.impl;

import com.example.board.domain.BoardV0;
import com.example.board.domain.CommentV0;
import com.example.board.dto.BoardDetailResponse;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.service.BoardService;
import com.example.board.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SOLID 원칙 적용:
 * 
 * SRP (Single Responsibility Principle): 
 * 게시글 관련 비즈니스 로직 처리만을 담당합니다.
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
public class BoardServiceImpl implements BoardService {
    
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    
    /**
     * 게시글 목록 조회
     * 비즈니스 로직: 삭제되지 않은 게시글만 조회
     */
    @Override
    public List<BoardV0> getBoardList() {
        return boardRepository.findAll();
    }
    
    /**
     * 게시글 상세 조회 (댓글 포함)
     * 비즈니스 로직: 게시글 존재 여부 확인 후 댓글과 함께 조회
     * OCP 적용: 예외를 통해 오류 상황을 처리
     * DIP 적용: Repository 추상화를 통해 데이터 접근
     */
    @Override
    public BoardDetailResponse getBoardWithComments(Long idx) {
        // 게시글 조회
        BoardV0 board = boardRepository.findById(idx)
                .orElseThrow(() -> new com.example.board.exception.BoardNotFoundException("게시글을 찾을 수 없습니다."));
        
        // 댓글 조회
        List<CommentV0> comments = commentRepository.findByBoardIdx(idx);
        
        return BoardDetailResponse.of(board, comments);
    }
    
    /**
     * 게시글 등록
     * 실무 원칙: Service에서 모든 비즈니스 로직과 검증 처리
     * - 입력값 검증
     * - 사용자 권한 확인
     * - 성공/실패 결과 반환
     */
    @Override
    public ApiResponse<Void> createBoard(BoardV0 board, String userId) {
        // 1. 입력값 검증
        if (board.getTitle() == null || board.getTitle().trim().isEmpty()) {
            return ApiResponse.failure("제목은 필수입니다.");
        }
        if (board.getTitle().length() > 100) {
            return ApiResponse.failure("제목은 100자 이내로 입력해주세요.");
        }
        if (board.getContent() == null || board.getContent().trim().isEmpty()) {
            return ApiResponse.failure("내용은 필수입니다.");
        }
        if (board.getContent().length() > 4000) {
            return ApiResponse.failure("내용은 4000자 이내로 입력해주세요.");
        }
        
        try {
            // 2. 게시글 데이터 설정
            board.setWriterId(userId);
            
            // 3. 저장 처리
            boardRepository.save(board);
            
            return ApiResponse.success("게시글이 성공적으로 등록되었습니다.");
            
        } catch (Exception e) {
            log.error("게시글 등록 실패 - 사용자: {}, 제목: {}, 오류: {}", userId, board.getTitle(), e.getMessage(), e);
            return ApiResponse.failure("게시글 등록 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 게시글 존재 여부 확인
     * 비즈니스 로직: 특정 게시글의 존재 여부를 확인
     * DIP 적용: Repository 추상화를 통해 데이터 확인
     */
    @Override
    public boolean existsBoard(Long idx) {
        return boardRepository.findById(idx).isPresent();
    }
    
    /**
     * 게시글 수정
     * 실무 원칙: Service에서 권한 검증과 비즈니스 로직 처리
     * - 게시글 존재 확인
     * - 작성자 권한 확인
     * - 입력값 검증
     * - 성공/실패 결과 반환
     */
    @Override
    public ApiResponse<Void> updateBoard(Long boardIdx, BoardV0 board, String userId) {
        // 1. 게시글 존재 확인
        BoardV0 existingBoard = boardRepository.findById(boardIdx).orElse(null);
        if (existingBoard == null) {
            return ApiResponse.failure("존재하지 않는 게시글입니다.");
        }
        
        // 2. 작성자 권한 확인
        if (!existingBoard.getWriterId().equals(userId)) {
            log.warn("권한 없는 수정 시도 - 게시글: {}, 시도자: {}", boardIdx, userId);
            return ApiResponse.failure("본인이 작성한 게시글만 수정할 수 있습니다.");
        }
        
        // 3. 입력값 검증
        if (board.getTitle() == null || board.getTitle().trim().isEmpty()) {
            return ApiResponse.failure("제목은 필수입니다.");
        }
        if (board.getTitle().length() > 100) {
            return ApiResponse.failure("제목은 100자 이내로 입력해주세요.");
        }
        if (board.getContent() == null || board.getContent().trim().isEmpty()) {
            return ApiResponse.failure("내용은 필수입니다.");
        }
        if (board.getContent().length() > 4000) {
            return ApiResponse.failure("내용은 4000자 이내로 입력해주세요.");
        }
        
        try {
            // 4. 수정할 데이터 설정
            board.setIdx(boardIdx);
            
            // 5. 수정 처리
            boardRepository.update(board);
            
            return ApiResponse.success("게시글이 성공적으로 수정되었습니다.");
            
        } catch (Exception e) {
            log.error("게시글 수정 실패 - 게시글: {}, 사용자: {}, 오류: {}", boardIdx, userId, e.getMessage(), e);
            return ApiResponse.failure("게시글 수정 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 게시글 삭제
     * 실무 원칙: Service에서 권한 검증과 비즈니스 로직 처리
     * - 게시글 존재 확인
     * - 작성자 권한 확인
     * - 논리 삭제 처리
     * - 성공/실패 결과 반환
     */
    @Override
    public ApiResponse<Void> deleteBoard(Long boardIdx, String userId) {
        // 1. 게시글 존재 확인
        BoardV0 board = boardRepository.findById(boardIdx).orElse(null);
        if (board == null) {
            return ApiResponse.failure("존재하지 않는 게시글입니다.");
        }
        
        // 2. 작성자 권한 확인
        if (!board.getWriterId().equals(userId)) {
            log.warn("권한 없는 삭제 시도 - 게시글: {}, 시도자: {}", boardIdx, userId);
            return ApiResponse.failure("본인이 작성한 게시글만 삭제할 수 있습니다.");
        }
        
        try {
            // 3. 논리 삭제 처리
            boardRepository.deleteById(boardIdx);
            
            return ApiResponse.success("게시글이 성공적으로 삭제되었습니다.");
            
        } catch (Exception e) {
            log.error("게시글 삭제 실패 - 게시글: {}, 사용자: {}, 오류: {}", boardIdx, userId, e.getMessage(), e);
            return ApiResponse.failure("게시글 삭제 중 오류가 발생했습니다.");
        }
    }
    
    /**
     * 게시글 작성자 권한 확인
     * SOLID 원칙 적용: SRP - 권한 검증의 단일 책임
     * 비즈니스 로직: 게시글 작성자와 요청자가 동일한지 확인
     * DIP 적용: Repository 추상화를 통해 데이터 조회
     * 
     * @param boardIdx 게시글 번호
     * @param userId 사용자 ID
     * @return 작성자가 맞으면 true, 아니면 false
     */
    @Override
    public boolean isOwner(Long boardIdx, String userId) {
        return boardRepository.findById(boardIdx)
                .map(board -> board.getWriterId().equals(userId))
                .orElse(false);
    }
}