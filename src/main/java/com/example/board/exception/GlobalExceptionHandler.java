package com.example.board.exception;

import com.example.board.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * SOLID 원칙 적용:
 * 
 * OCP (Open-Closed Principle):
 * 새로운 예외 타입이 추가되어도 기존 핸들러를 수정하지 않고 새로운 핸들러만 추가하면 됩니다.
 * 
 * SRP (Single Responsibility Principle):
 * 전역 예외 처리만을 담당합니다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 입력값 검증 실패 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .success(false)
                .message("입력값 검증에 실패했습니다.")
                .data(errors)
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 게시글을 찾을 수 없는 경우 예외 처리
     * OCP 적용: 새로운 예외 타입 추가
     */
    @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleBoardNotFoundException(BoardNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.failure(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 댓글을 찾을 수 없는 경우 예외 처리
     * OCP 적용: 새로운 예외 타입 추가
     */
    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleCommentNotFoundException(CommentNotFoundException ex) {
        ApiResponse<Void> response = ApiResponse.failure(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 외래키 제약조건 위반 등 데이터베이스 무결성 오류 처리
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        String errorMessage;
        
        if (message.contains("foreign key constraint")) {
            errorMessage = "존재하지 않는 게시글에는 댓글을 등록할 수 없습니다.";
        } else if (message.contains("Duplicate entry")) {
            errorMessage = "중복된 데이터입니다.";
        } else {
            errorMessage = "데이터베이스 제약조건을 위반했습니다.";
        }
        
        ApiResponse<Void> response = ApiResponse.failure(errorMessage);
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * 인증 관련 예외 처리 (로그인 실패, 토큰 오류 등)
     * OCP 적용: 새로운 예외 타입 추가
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleAuthenticationException(RuntimeException ex) {
        String message = ex.getMessage();
        
        // 실제 오류 정보 로깅
        log.error("RuntimeException 발생: {}", message, ex);
        
        // 인증 관련 오류 메시지 판단
        if (message.contains("사용자를 찾을 수 없습니다") || 
            message.contains("비밀번호가 일치하지 않습니다") ||
            message.contains("비활성화된 계정입니다")) {
            
            ApiResponse<Void> response = ApiResponse.failure(message);
            return ResponseEntity.badRequest().body(response);
        }
        
        // 기타 RuntimeException
        ApiResponse<Void> response = ApiResponse.failure("처리 중 오류가 발생했습니다: " + message);
        return ResponseEntity.internalServerError().body(response);
    }

    /**
     * 일반적인 모든 예외 처리 (마지막 안전망)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
        // 실제 오류 정보 로깅
        log.error("예상치 못한 Exception 발생: {}", ex.getMessage(), ex);
        
        ApiResponse<Void> response = ApiResponse.failure("서버에서 오류가 발생했습니다: " + ex.getMessage());
        return ResponseEntity.internalServerError().body(response);
    }
}