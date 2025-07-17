package com.example.board.mapper;

import com.example.board.domain.BoardV0;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BoardMapper {
    void insertBoard(BoardV0 boardV0);
}
