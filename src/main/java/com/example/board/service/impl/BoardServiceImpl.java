package com.example.board.service.impl;

import com.example.board.domain.BoardV0;
import com.example.board.domain.CommentV0;
import com.example.board.dto.BoardDetailResponse;
import com.example.board.repository.BoardRepository;
import com.example.board.repository.CommentRepository;
import com.example.board.service.BoardService;
import lombok.RequiredArgsConstructor;
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
     * 비즈니스 로직: 게시글 데이터 검증 및 저장
     * DIP 적용: Repository 추상화를 통해 데이터 저장
     */
    @Override
    public void createBoard(BoardV0 board) {
        boardRepository.save(board);
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
     * 게시글 수정 (ISP 적용: BoardWriteService 인터페이스 구현)
     * 향후 확장을 위한 메서드
     * DIP 적용: Repository 추상화를 통해 데이터 수정
     */
    @Override
    public void updateBoard(BoardV0 board) {
        boardRepository.update(board);
    }
    
    /**
     * 게시글 삭제 (ISP 적용: BoardWriteService 인터페이스 구현)
     * 비즈니스 로직: 게시글 논리 삭제 처리
     * DIP 적용: Repository 추상화를 통해 데이터 삭제
     */
    @Override
    public void deleteBoard(Long idx) {
        boardRepository.deleteById(idx);
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