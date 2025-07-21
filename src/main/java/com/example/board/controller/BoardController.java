package com.example.board.controller;

import com.example.board.domain.BoardV0;
import com.example.board.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardMapper boardMapper;

    @PostMapping
    public ResponseEntity<?> insertBoard(@Valid @RequestBody BoardV0 board) {
        // @Valid 어노테이션으로 검증 실패 시 자동으로 MethodArgumentNotValidException 발생
        // GlobalExceptionHandler에서 처리됨
        
        boardMapper.insertBoard(board);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "게시글이 성공적으로 등록되었습니다.");
        
        return ResponseEntity.ok(response);
    }
}
