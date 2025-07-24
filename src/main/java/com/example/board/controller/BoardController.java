package com.example.board.controller;

import com.example.board.domain.BoardV0;
import com.example.board.dto.ApiResponse;
import com.example.board.dto.BoardDetailResponse;
import com.example.board.service.BoardService;
import com.example.board.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
 * 구체적인 구현체가 아닌 인터페이스(BoardService)에 의존합니다.
 * 
 * OCP (Open-Closed Principle):
 * Service 인터페이스를 통해 확장에는 열려있고 수정에는 닫혀있습니다.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * 게시글 목록 조회 - GET /board
     * 책임: HTTP 요청 처리 및 응답 반환
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardV0>>> getBoardList() {
        List<BoardV0> boardList = boardService.getBoardList();
        
        ApiResponse<List<BoardV0>> response = ApiResponse.success(
            "게시글 목록 조회가 완료되었습니다.", 
            boardList, 
            boardList.size()
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 상세 조회 (댓글 포함) - GET /board/{idx}
     * 책임: HTTP 요청 처리 및 응답 반환
     * OCP 적용: 예외 처리는 GlobalExceptionHandler에 위임
     */
    @GetMapping("/{idx}")
    public ResponseEntity<ApiResponse<BoardDetailResponse>> getBoardWithComments(@PathVariable Long idx) {
        BoardDetailResponse boardDetail = boardService.getBoardWithComments(idx);
        
        ApiResponse<BoardDetailResponse> response = ApiResponse.success(
            "게시글 상세 조회가 완료되었습니다.", 
            boardDetail
        );
        
        return ResponseEntity.ok(response);
    }

    /**
     * 게시글 등록 - POST /board
     * 책임: HTTP 요청 처리 및 응답 반환
     * 인증된 사용자만 게시글을 작성할 수 있음
     * JWT 토큰에서 사용자 ID를 자동으로 추출하여 설정
     * (작성자명은 DB JOIN을 통해 실시간으로 조회)
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> insertBoard(
            @RequestBody BoardV0 board, 
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID만 추출
        String token = getTokenFromRequest(request);
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        
        // 게시글에 작성자 ID만 설정 (작성자명은 DB JOIN으로 조회)
        board.setWriterId(userId);
        
        boardService.createBoard(board);
        
        ApiResponse<Void> response = ApiResponse.success("게시글이 성공적으로 등록되었습니다.");
        
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
}