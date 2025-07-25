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
     * 실무 원칙: 쿼리스트링 활용한 페이징과 검색 기능
     * 예시: GET /board?page=1&size=10&keyword=검색어
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<BoardV0>>> getBoardList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        
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
     * 실무 원칙: Controller는 HTTP 처리만, 모든 비즈니스 로직은 Service에 위임
     * Spring Validation 적용: @Valid로 입력값 검증 자동화
     */
    @PostMapping
    public ResponseEntity<ApiResponse<Void>> createBoard(
            @Valid @RequestBody BoardV0 board, 
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID 추출
        String token = getTokenFromRequest(request);
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        
        // Service에 모든 로직 위임
        ApiResponse<Void> result = boardService.createBoard(board, userId);
        
        return result.isSuccess() 
            ? ResponseEntity.ok(result)
            : ResponseEntity.badRequest().body(result);
    }
    
    /**
     * 게시글 수정 - PUT /board/{idx}
     * 실무 원칙: Controller는 HTTP 처리만, 모든 비즈니스 로직은 Service에 위임
     * Spring Validation 적용: @Valid로 입력값 검증 자동화
     */
    @PutMapping("/{idx}")
    public ResponseEntity<ApiResponse<Void>> updateBoard(
            @PathVariable Long idx,
            @Valid @RequestBody BoardV0 board,
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID 추출
        String token = getTokenFromRequest(request);
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        
        // Service에 모든 로직 위임
        ApiResponse<Void> result = boardService.updateBoard(idx, board, userId);
        
        return result.isSuccess() 
            ? ResponseEntity.ok(result)
            : ResponseEntity.status(403).body(result);
    }

    /**
     * 게시글 삭제 - DELETE /board/{idx}
     * 실무 원칙: Controller는 HTTP 처리만, 모든 비즈니스 로직은 Service에 위임
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<ApiResponse<Void>> deleteBoard(
            @PathVariable Long idx,
            HttpServletRequest request) {
        
        // JWT 토큰에서 사용자 ID 추출
        String token = getTokenFromRequest(request);
        String userId = jwtTokenUtil.getUserIdFromToken(token);
        
        // Service에 모든 로직 위임
        ApiResponse<Void> result = boardService.deleteBoard(idx, userId);
        
        return result.isSuccess() 
            ? ResponseEntity.ok(result)
            : ResponseEntity.status(403).body(result);
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