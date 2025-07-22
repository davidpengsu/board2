package com.example.board.domain;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class BoardV0 {
    
    // 조회 시에만 사용되는 필드 (등록 시에는 DB에서 자동 생성)
    private Long idx;                   // 게시글 고유 ID (AUTO_INCREMENT)
    private LocalDateTime regDate;      // 등록일시 (NOW())
    private String delYn;               // 삭제 여부 ('N')
    
    // 계산된 필드들 (SELECT 시에만 계산되는 값들)
    private Integer commentCount;       // 댓글 개수 (LEFT JOIN + COUNT)
    private Integer isNew;              // 신규 게시글 여부 (1: 오늘 작성, 0: 이전 작성)
    
    // 등록/수정 시 필수 입력 필드
    @NotBlank(message = "제목은 필수 입력값입니다.")
    @Size(max = 100, message = "제목은 100자를 초과할 수 없습니다.")
    private String title;
    
    @NotBlank(message = "내용은 필수 입력값입니다.")
    @Size(max = 2000, message = "내용은 2000자를 초과할 수 없습니다.")
    private String content;
    
    @NotBlank(message = "작성자명은 필수 입력값입니다.")
    @Size(max = 50, message = "작성자명은 50자를 초과할 수 없습니다.")
    private String writerNm;
}

