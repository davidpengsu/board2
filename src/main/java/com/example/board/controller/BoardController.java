package com.example.board.controller;

import com.example.board.domain.BoardV0;
import com.example.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")

public class BoardController {

    private final BoardMapper boardMapper;

    @PostMapping
    public String inserBoard(@RequestBody BoardV0 board) {
        boardMapper.insertBoard(board);
        return "등록에 성공";
    }
}
