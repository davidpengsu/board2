package com.example.board.controller;

import com.example.board.domain.CommentV0;
import com.example.board.dto.ApiResponse;
import com.example.board.service.CommentService;
import com.example.board.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * SOLID 원칙 적용:
 * 
 * SRP (Single Responsibility Principle): 
 * HTTP 요청/응답 처리만을 담당합니다. 비즈니스 로직은 Service 계층에 위임합니다.
 * 
 * DIP (Dependency Inversion Principle):
 * 구체적인 구현체가 아닌 인터페이스(CommentService)에 의존합니다.
 * 
 * OCP (Open-Closed Principle):
 * Service 인터페이스를 통해 확장에는 열려있고 수정에는 닫혀있습니다.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 댓글 등록 - POST /comment
     * 책임: HTTP 요청 처리 및 응답 반환
     * 인증된 사용자만 댓글을 작성할 수 있음
     * JWT 토큰에서 사용자 ID를 자동으로 추출하여 설정
     * (작성자명은 DB JOIN을 통해 실시간으로 조회)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> insertComment(
            @RequestBody CommentV0 comment,
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID만 추출
        String token = getTokenFromRequest(request);
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        
        // 댓글에 작성자 ID만 설정 (작성자명은 DB JOIN으로 조회)
        comment.setWriterId(userId);
        
        commentService.createComment(comment);
        
        ApiResponse<Void> response = ApiResponse.success("댓글이 성공적으로 등록되었습니다.");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * HTTP 요청에서 JWT 토큰 추출
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * 특정 게시글의 댓글 목록 조회 - GET /comment/board/{boardIdx}
     * 책임: HTTP 요청 처리 및 응답 반환
     */
    @GetMapping("/board/{boardIdx}")
    public ResponseEntity<ApiResponse<List<CommentV0>>> getCommentsByBoardIdx(@PathVariable Long boardIdx) {
        List<CommentV0> commentList = commentService.getCommentsByBoardIdx(boardIdx);
        
        ApiResponse<List<CommentV0>> response = ApiResponse.success(
            "댓글 목록 조회가 완료되었습니다.", 
            commentList, 
            commentList.size()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 모든 댓글 목록 조회 - GET /comment
     * 책임: HTTP 요청 처리 및 응답 반환
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<CommentV0>>> getAllComments() {
        List<CommentV0> commentList = commentService.getAllComments();
        
        ApiResponse<List<CommentV0>> response = ApiResponse.success(
            "전체 댓글 목록 조회가 완료되었습니다.", 
            commentList, 
            commentList.size()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 댓글 삭제 - DELETE /comment/{idx}
     * 책임: HTTP 요청 처리 및 응답 반환
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long idx) {
        commentService.deleteComment(idx);
        
        ApiResponse<Void> response = ApiResponse.success("댓글이 성공적으로 삭제되었습니다.");
        
        return ResponseEntity.ok(response);
    }
}