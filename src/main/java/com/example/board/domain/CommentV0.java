package com.example.board.domain;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class CommentV0 {
    
    // 조회 시에만 사용되는 필드 (등록 시에는 DB에서 자동 생성)
    private Long idx;                   // 댓글 고유 ID (AUTO_INCREMENT)
    private LocalDateTime regDate;      // 등록일시 (NOW())
    private String delYn;               // 삭제 여부 ('N')
    
    // 등록 시 필수 입력 필드
    @NotNull(message = "게시글 번호는 필수입니다.")
    private Long boardIdx;              // 게시글 번호 (외래키)
    
    @NotBlank(message = "댓글 내용은 필수 입력값입니다.")
    @Size(max = 500, message = "댓글은 500자를 초과할 수 없습니다.")
    private String comment;             // 댓글 내용
    
    @NotBlank(message = "작성자명은 필수 입력값입니다.")
    @Size(max = 50, message = "작성자명은 50자를 초과할 수 없습니다.")
    private String writerName;          // 댓글 작성자명
}