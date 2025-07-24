package com.example.board.dto;

import com.example.board.domain.BoardV0;
import com.example.board.domain.CommentV0;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * 게시글 상세 조회 응답 데이터 구조 관리만을 담당합니다.
 * 게시글과 댓글 정보를 하나의 응답으로 묶어서 전달하는 역할을 합니다.
 */
@Getter
@Builder
public class BoardDetailResponse {
    
    private final BoardV0 board;               // 게시글 정보
    private final List<CommentV0> comments;    // 댓글 목록
    private final Integer commentCount;        // 댓글 개수
    
    public static BoardDetailResponse of(BoardV0 board, List<CommentV0> comments) {
        return BoardDetailResponse.builder()
                .board(board)
                .comments(comments)
                .commentCount(comments != null ? comments.size() : 0)
                .build();
    }
}