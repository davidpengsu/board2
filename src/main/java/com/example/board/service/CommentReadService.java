package com.example.board.service;

import com.example.board.domain.CommentV0;

import java.util.List;

/**
 * SOLID 원칙 적용: ISP (Interface Segregation Principle)
 * 댓글 조회 관련 기능만을 정의한 인터페이스입니다.
 * 클라이언트가 사용하지 않는 메서드에 의존하지 않도록 인터페이스를 분리했습니다.
 */
public interface CommentReadService {
    
    /**
     * 특정 게시글의 댓글 목록 조회
     * @param boardIdx 게시글 번호
     * @return 댓글 목록
     */
    List<CommentV0> getCommentsByBoardIdx(Long boardIdx);
    
    /**
     * 전체 댓글 목록 조회
     * @return 전체 댓글 목록
     */
    List<CommentV0> getAllComments();
}