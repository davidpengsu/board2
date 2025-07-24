package com.example.board.service;

/**
 * SOLID 원칙 적용: 
 * 
 * DIP (Dependency Inversion Principle)
 * 고수준 모듈(Controller)이 저수준 모듈(Service 구현체)에 의존하지 않도록
 * 추상화(인터페이스)에 의존하게 합니다.
 * 
 * ISP (Interface Segregation Principle)
 * 읽기와 쓰기 인터페이스를 분리하여 클라이언트가 필요한 기능만 의존하도록 합니다.
 * 예: 읽기 전용 클라이언트는 CommentReadService만 의존하면 됩니다.
 */
public interface CommentService extends CommentReadService, CommentWriteService {
    // 조합 인터페이스: 읽기와 쓰기 기능을 모두 제공
    // 기존 클라이언트 코드의 호환성을 위해 유지
}