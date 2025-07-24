package com.example.board.dto;

import lombok.Builder;
import lombok.Getter;

/**
 * SOLID 원칙 적용: SRP (Single Responsibility Principle)
 * 이 클래스는 API 응답 구조 관리라는 단일 책임만을 가집니다.
 * 모든 API 응답의 일관성을 보장하며, 응답 형식 변경시 이 클래스만 수정하면 됩니다.
 */
@Getter
@Builder
public class ApiResponse<T> {
    
    private final boolean success;      // 성공 여부
    private final String message;       // 응답 메시지
    private final T data;              // 실제 데이터
    private final Integer totalCount;   // 전체 개수 (목록 조회시 사용)
    
    /**
     * 성공 응답 생성 (데이터 포함)
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * 성공 응답 생성 (목록 데이터 + 개수 포함)
     */
    public static <T> ApiResponse<T> success(String message, T data, Integer totalCount) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .totalCount(totalCount)
                .build();
    }
    
    /**
     * 성공 응답 생성 (메시지만)
     */
    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .build();
    }
    
    /**
     * 실패 응답 생성
     */
    public static <T> ApiResponse<T> failure(String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .build();
    }
}