package com.example.board.service;

import com.example.board.domain.CommentV0;
import com.example.board.dto.ApiResponse;

import java.util.List;

/**
 * 실무 원칙: 단순하고 실용적인 인터페이스 설계
 * 과도한 인터페이스 분리보다 명확한 하나의 서비스 인터페이스 사용
 * CommentReadService, CommentWriteService 분리 제거
 */
public interface CommentService {
    
    // 조회 기능
    List<CommentV0> getCommentsByBoardIdx(Long boardIdx);
    List<CommentV0> getAllComments();
    
    // 등록/수정/삭제 기능
    ApiResponse<Void> createComment(CommentV0 comment, String userId);
    ApiResponse<Void> updateComment(Long commentIdx, CommentV0 comment, String userId);
    ApiResponse<Void> deleteComment(Long commentIdx, String userId);
}