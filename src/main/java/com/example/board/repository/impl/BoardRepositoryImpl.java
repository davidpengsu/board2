package com.example.board.repository.impl;

import com.example.board.domain.BoardV0;
import com.example.board.mapper.BoardMapper;
import com.example.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * SOLID 원칙 적용: DIP (Dependency Inversion Principle)
 * 
 * 이 클래스는 Repository 인터페이스의 구현체로서,
 * 실제로는 MyBatis Mapper를 사용하지만 상위 계층(Service)은
 * 이 구현 세부사항을 알 필요가 없습니다.
 * 
 * 향후 JPA, JDBC Template 등 다른 데이터 접근 기술로 변경시에도
 * 이 클래스만 교체하면 되고, Service 계층은 변경할 필요가 없습니다.
 */
@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepository {
    
    private final BoardMapper boardMapper;
    
    /**
     * 게시글 등록
     */
    @Override
    public void save(BoardV0 board) {
        boardMapper.insertBoard(board);
    }
    
    /**
     * 게시글 목록 조회 (삭제되지 않은 것만)
     */
    @Override
    public List<BoardV0> findAll() {
        return boardMapper.selectBoardList();
    }
    
    /**
     * 게시글 ID로 조회
     */
    @Override
    public Optional<BoardV0> findById(Long idx) {
        List<BoardV0> boardList = boardMapper.selectBoardList();
        return boardList.stream()
                .filter(board -> board.getIdx().equals(idx))
                .findFirst();
    }
    
    /**
     * 게시글 수정 (향후 구현)
     */
    @Override
    public void update(BoardV0 board) {
        // 향후 BoardMapper에 updateBoard 메서드 추가 후 구현
        throw new UnsupportedOperationException("게시글 수정 기능은 아직 구현되지 않았습니다.");
    }
    
    /**
     * 게시글 삭제 (논리 삭제, 향후 구현)
     */
    @Override
    public void deleteById(Long idx) {
        // 향후 BoardMapper에 deleteBoard 메서드 추가 후 구현
        throw new UnsupportedOperationException("게시글 삭제 기능은 아직 구현되지 않았습니다.");
    }
}