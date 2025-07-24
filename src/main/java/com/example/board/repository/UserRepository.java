package com.example.board.repository;

import com.example.board.domain.User;

import java.util.Optional;

/**
 * SOLID 원칙 적용: DIP (Dependency Inversion Principle)
 * Service 계층이 구체적인 MyBatis Mapper에 직접 의존하지 않고,
 * 이 추상화된 Repository 인터페이스에 의존하도록 합니다.
 */
public interface UserRepository {
    
    /**
     * 사용자 ID로 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 정보 (Optional)
     */
    Optional<User> findByUserId(String userId);
    
    /**
     * 사용자 저장 (향후 확장용)
     * @param user 사용자 정보
     */
    void save(User user);
}