package com.example.board.service;

import com.example.board.domain.BoardV0;
import com.example.board.dto.BoardDetailResponse;

import java.util.List;

/**
 * SOLID 원칙 적용: ISP (Interface Segregation Principle)
 * 게시글 조회 관련 기능만을 정의한 인터페이스입니다.
 * 클라이언트가 사용하지 않는 메서드에 의존하지 않도록 인터페이스를 분리했습니다.
 */
public interface BoardReadService {
    
    /**
     * 게시글 목록 조회
     * @return 게시글 목록
     */
    List<BoardV0> getBoardList();
    
    /**
     * 게시글 상세 조회 (댓글 포함)
     * @param idx 게시글 번호
     * @return 게시글 상세 정보 (댓글 포함)
     */
    BoardDetailResponse getBoardWithComments(Long idx);
    
    /**
     * 게시글 존재 여부 확인
     * @param idx 게시글 번호
     * @return 존재 여부
     */
    boolean existsBoard(Long idx);
}