package com.example.board.controller;

import com.example.board.domain.BoardV0;
import com.example.board.domain.CommentV0;
import com.example.board.mapper.BoardMapper;
import com.example.board.mapper.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardMapper boardMapper;
    private final CommentMapper commentMapper;

    // 게시글 목록 조회 - GET /board
    @GetMapping
    public ResponseEntity<?> getBoardList() {
        
        List<BoardV0> boardList = boardMapper.selectBoardList();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "게시글 목록 조회가 완료되었습니다.");
        response.put("data", boardList);
        response.put("totalCount", boardList.size());
        
        return ResponseEntity.ok(response);
    }

    // 게시글 상세 조회 (댓글 포함) - GET /board/{idx}
    @GetMapping("/{idx}")
    public ResponseEntity<?> getBoardWithComments(@PathVariable Long idx) {

        List<BoardV0> boardList = boardMapper.selectBoardList();
        BoardV0 board = boardList.stream()
                .filter(b -> b.getIdx().equals(idx))
                .findFirst()
                .orElse(null);
        
        if (board == null) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "게시글을 찾을 수 없습니다.");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        List<CommentV0> commentList = commentMapper.selectCommentsByBoardIdx(idx);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "게시글 상세 조회가 완료되었습니다.");
        response.put("board", board);
        response.put("comments", commentList);
        response.put("commentCount", commentList.size());
        
        return ResponseEntity.ok(response);
    }

    // 게시글 등록 - POST /board
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
