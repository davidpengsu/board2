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
}
