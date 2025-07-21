package com.example.board.domain;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class BoardV0 {
    
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

