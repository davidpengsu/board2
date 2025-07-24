package com.example.board.service;

import com.example.board.domain.BoardV0;

/**
 * SOLID 원칙 적용: ISP (Interface Segregation Principle)
 * 게시글 쓰기 관련 기능만을 정의한 인터페이스입니다.
 * 클라이언트가 사용하지 않는 메서드에 의존하지 않도록 인터페이스를 분리했습니다.
 */
public interface BoardWriteService {
    
    /**
     * 게시글 등록
     * @param board 등록할 게시글 정보
     */
    void createBoard(BoardV0 board);
    
    /**
     * 게시글 수정 (향후 확장을 위한 메서드)
     * @param board 수정할 게시글 정보
     */
    void updateBoard(BoardV0 board);
    
    /**
     * 게시글 삭제
     * @param idx 삭제할 게시글 번호
     */
    void deleteBoard(Long idx);
}