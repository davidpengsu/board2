package com.example.board.service;

import com.example.board.domain.BoardV0;
import com.example.board.dto.ApiResponse;
import com.example.board.dto.BoardDetailResponse;

import java.util.List;

/**
 * 실무 원칙: 단순하고 실용적인 인터페이스 설계
 * 과도한 인터페이스 분리보다 명확한 하나의 서비스 인터페이스 사용
 * BoardReadService, BoardWriteService 분리 제거
 */
public interface BoardService {
    
    // 조회 기능
    List<BoardV0> getBoardList();
    BoardDetailResponse getBoardWithComments(Long idx);
    boolean existsBoard(Long idx);
    
    // 등록/수정/삭제 기능
    ApiResponse<Void> createBoard(BoardV0 board, String userId);
    ApiResponse<Void> updateBoard(Long boardIdx, BoardV0 board, String userId);
    ApiResponse<Void> deleteBoard(Long boardIdx, String userId);
    
    // 권한 확인 (내부 사용)
    boolean isOwner(Long boardIdx, String userId);
}