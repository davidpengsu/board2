package com.example.board.mapper;

import com.example.board.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

/**
 * SOLID 원칙 적용: ISP (Interface Segregation Principle)
 * 사용자 데이터 접근에 필요한 메서드만 정의합니다.
 */
@Mapper
public interface UserMapper {
    
    /**
     * 사용자 ID로 사용자 조회
     * @param userId 사용자 ID
     * @return 사용자 정보 (Optional)
     */
    User selectUserByUserId(String userId);
    
    /**
     * 사용자 등록 (향후 확장을 위해 정의, 현재는 사용하지 않음)
     * @param user 사용자 정보
     */
    void insertUser(User user);
}