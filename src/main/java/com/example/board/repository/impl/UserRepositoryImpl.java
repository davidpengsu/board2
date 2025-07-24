package com.example.board.repository.impl;

import com.example.board.domain.User;
import com.example.board.mapper.UserMapper;
import com.example.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * SOLID 원칙 적용: DIP (Dependency Inversion Principle)
 * 
 * 이 클래스는 Repository 인터페이스의 구현체로서,
 * 실제로는 MyBatis Mapper를 사용하지만 상위 계층(Service)은
 * 이 구현 세부사항을 알 필요가 없습니다.
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    
    private final UserMapper userMapper;
    
    /**
     * 사용자 ID로 사용자 조회
     */
    @Override
    public Optional<User> findByUserId(String userId) {
        User user = userMapper.selectUserByUserId(userId);
        return Optional.ofNullable(user);
    }
    
    /**
     * 사용자 저장 (향후 확장용)
     */
    @Override
    public void save(User user) {
        userMapper.insertUser(user);
    }
}