package com.example.board.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "입력값 검증에 실패했습니다.");
        response.put("errors", errors);
        
        return ResponseEntity.badRequest().body(response);
    }

    // 외래키 제약조건 위반 등 데이터베이스 무결성 오류 처리
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        
        String message = ex.getMessage();
        if (message.contains("foreign key constraint")) {
            response.put("message", "존재하지 않는 게시글에는 댓글을 등록할 수 없습니다.");
            response.put("errorCode", "INVALID_BOARD_REFERENCE");
        } else if (message.contains("Duplicate entry")) {
            response.put("message", "중복된 데이터입니다.");
            response.put("errorCode", "DUPLICATE_DATA");
        } else {
            response.put("message", "데이터베이스 제약조건을 위반했습니다.");
            response.put("errorCode", "DATA_INTEGRITY_VIOLATION");
        }
        
        return ResponseEntity.badRequest().body(response);
    }

    // 일반적인 모든 예외 처리 (마지막 안전망)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "서버에서 오류가 발생했습니다.");
        response.put("errorCode", "INTERNAL_SERVER_ERROR");
        
        // 개발 환경에서는 상세 오류 메시지 포함 (운영에서는 제거해야 함)
        response.put("detail", ex.getMessage());
        
        return ResponseEntity.internalServerError().body(response);
    }
}