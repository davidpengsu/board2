-- JWT 로그인 기능이 적용된 게시판 데이터베이스 설정
-- 실행 순서: 1. 테이블 생성 2. 샘플 사용자 데이터 삽입

-- 1. 사용자 테이블 생성
CREATE TABLE IF NOT EXISTS users (
    idx BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '사용자 고유 ID',
    user_id VARCHAR(20) NOT NULL UNIQUE COMMENT '로그인 ID',
    password VARCHAR(100) NOT NULL COMMENT '암호화된 비밀번호',
    username VARCHAR(50) NOT NULL COMMENT '사용자 이름',
    email VARCHAR(100) NOT NULL COMMENT '이메일 주소',
    role VARCHAR(20) DEFAULT 'USER' COMMENT '사용자 역할 (USER, ADMIN)',
    active_yn CHAR(1) DEFAULT 'Y' COMMENT '계정 활성화 여부',
    reg_date DATETIME DEFAULT NOW() COMMENT '등록일시',
    del_yn CHAR(1) DEFAULT 'N' COMMENT '삭제 여부'
) COMMENT '사용자 정보 테이블';

-- 2. 게시글 테이블에 writer_id 컬럼 추가 (이미 존재하는 경우)
ALTER TABLE t_board 
ADD COLUMN IF NOT EXISTS writer_id VARCHAR(20) COMMENT '작성자 ID (users.user_id 참조)';

ALTER TABLE t_comment ADD COLUMN writer_id VARCHAR(20) COMMENT '작성자 ID (users.user_id 참조)';

-- 3. 댓글 테이블에 writer_id 컬럼 추가 (이미 존재하는 경우)  
ALTER TABLE t_comment 
ADD COLUMN IF NOT EXISTS writer_id VARCHAR(20) COMMENT '작성자 ID (users.user_id 참조)';

-- 4. 샘플 사용자 데이터 삽입
-- 비밀번호는 BCrypt로 암호화된 값 (원본: "password123!")
INSERT INTO users (user_id, password, username, email, role, active_yn) VALUES 
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFGjO6NJclk0XLyLCN6Z.BS', '관리자', 'admin@example.com', 'ADMIN', 'Y'),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFGjO6NJclk0XLyLCN6Z.BS', '사용자1', 'user1@example.com', 'USER', 'Y'),
('user2', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFGjO6NJclk0XLyLCN6Z.BS', '사용자2', 'user2@example.com', 'USER', 'Y'),
('testuser', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iYqiSfFGjO6NJclk0XLyLCN6Z.BS', '테스트사용자', 'test@example.com', 'USER', 'Y')
ON DUPLICATE KEY UPDATE 
    password = VALUES(password),
    username = VALUES(username),
    email = VALUES(email);

-- 5. 기존 게시글 데이터에 writer_id 업데이트 (필요한 경우)
UPDATE t_board SET writer_id = 'admin' WHERE writer_id IS NULL AND writerNm = '관리자';
UPDATE t_board SET writer_id = 'user1' WHERE writer_id IS NULL AND writerNm = '사용자1';
UPDATE t_board SET writer_id = 'user2' WHERE writer_id IS NULL AND writerNm = '사용자2';
UPDATE t_board SET writer_id = 'testuser' WHERE writer_id IS NULL;

-- 6. 기존 댓글 데이터에 writer_id 업데이트 (필요한 경우)
UPDATE t_comment SET writer_id = 'admin' WHERE writer_id IS NULL AND writerName = '관리자';
UPDATE t_comment SET writer_id = 'user1' WHERE writer_id IS NULL AND writerName = '사용자1';  
UPDATE t_comment SET writer_id = 'user2' WHERE writer_id IS NULL AND writerName = '사용자2';
UPDATE t_comment SET writer_id = 'testuser' WHERE writer_id IS NULL;

-- 7. 인덱스 생성 (성능 최적화)
CREATE INDEX IF NOT EXISTS idx_users_user_id ON users(user_id);
CREATE INDEX IF NOT EXISTS idx_users_active ON users(active_yn, del_yn);
CREATE INDEX IF NOT EXISTS idx_board_writer_id ON t_board(writer_id);
CREATE INDEX IF NOT EXISTS idx_comment_writer_id ON t_comment(writer_id);

-- 사용자 계정 정보
-- ===============================================
-- 사용자 ID: admin      | 비밀번호: password123!
-- 사용자 ID: user1      | 비밀번호: password123!  
-- 사용자 ID: user2      | 비밀번호: password123!
-- 사용자 ID: testuser   | 비밀번호: password123!
-- ===============================================