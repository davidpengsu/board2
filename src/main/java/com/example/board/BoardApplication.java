package com.example.board;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.example.board.mapper")  // 이게 핵심!!!
@SpringBootApplication
public class BoardApplication {
	public static void main(String[] args) {
		SpringApplication.run(BoardApplication.class, args);
	}
}
