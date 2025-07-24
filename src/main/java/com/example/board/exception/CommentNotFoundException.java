package com.example.board.exception;

/**
 * SOLID 원칙 적용: OCP (Open-Closed Principle)
 * 새로운 예외 타입 추가시 기존 코드를 수정하지 않고 확장할 수 있습니다.
 * 댓글을 찾을 수 없는 경우에 발생하는 비즈니스 예외입니다.
 */
public class CommentNotFoundException extends RuntimeException {
    
    public CommentNotFoundException(String message) {
        super(message);
    }
    
    public CommentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}