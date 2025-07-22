package com.example.board.controller;

import com.example.board.domain.CommentV0;
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
@RequestMapping("/comment")
public class CommentController {

    private final CommentMapper commentMapper;

    // 댓글 등록 - POST /comment
    @PostMapping
    public ResponseEntity<?> insertComment(@Valid @RequestBody CommentV0 comment) {
        
        commentMapper.insertComment(comment);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "댓글이 성공적으로 등록되었습니다.");
        
        return ResponseEntity.ok(response);
    }

    // 특정 게시글의 댓글 목록 조회 - GET /comment/board/{boardIdx}
    @GetMapping("/board/{boardIdx}")
    public ResponseEntity<?> getCommentsByBoardIdx(@PathVariable Long boardIdx) {
        
        List<CommentV0> commentList = commentMapper.selectCommentsByBoardIdx(boardIdx);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "댓글 목록 조회가 완료되었습니다.");
        response.put("data", commentList);
        response.put("totalCount", commentList.size());
        
        return ResponseEntity.ok(response);
    }

    // 모든 댓글 목록 조회 - GET /comment
    @GetMapping
    public ResponseEntity<?> getAllComments() {
        
        List<CommentV0> commentList = commentMapper.selectAllComments();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "전체 댓글 목록 조회가 완료되었습니다.");
        response.put("data", commentList);
        response.put("totalCount", commentList.size());
        
        return ResponseEntity.ok(response);
    }

    // 댓글 삭제 - DELETE /comment/{idx}
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteComment(@PathVariable Long idx) {
        
        commentMapper.deleteComment(idx);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "댓글이 성공적으로 삭제되었습니다.");
        
        return ResponseEntity.ok(response);
    }
}