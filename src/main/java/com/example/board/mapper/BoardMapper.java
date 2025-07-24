package com.example.board.mapper;

import com.example.board.domain.BoardV0;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface BoardMapper {
    
    // 게시글 등록 (INSERT)
    void insertBoard(BoardV0 boardV0);
    
    // 게시글 목록 조회 (SELECT) - 삭제되지 않은 게시글만
    List<BoardV0> selectBoardList();
    
    // 게시글 단건 조회 (ID로 직접 조회)
    BoardV0 selectBoardById(Long idx);
    
    // 게시글 삭제 (논리 삭제) - delYn을 'Y'로 변경
    void deleteBoard(Long idx);
    
    // 게시글 수정 - 제목과 내용만 수정 가능
    void updateBoard(BoardV0 boardV0);
}
