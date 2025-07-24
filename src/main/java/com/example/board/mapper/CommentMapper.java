package com.example.board.mapper;

import com.example.board.domain.CommentV0;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CommentMapper {
    
    // 댓글 등록 (INSERT)
    void insertComment(CommentV0 comment);
    
    // 특정 게시글의 댓글 목록 조회 (SELECT) - 삭제되지 않은 댓글만
    List<CommentV0> selectCommentsByBoardIdx(Long boardIdx);
    
    // 모든 댓글 목록 조회 (SELECT) - 삭제되지 않은 댓글만
    List<CommentV0> selectAllComments();
    
    // 댓글 단건 조회 (ID로 직접 조회)
    CommentV0 selectCommentById(Long idx);
    
    // 댓글 삭제 (논리 삭제: delYn = 'Y')
    void deleteComment(Long idx);
}