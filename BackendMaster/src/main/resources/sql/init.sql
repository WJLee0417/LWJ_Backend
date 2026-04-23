/*
  Step-up Backend: Mock-to-SQL Project 
  초기 데이터베이스 구축 스크립트 (v2.1)
*/

-- 1. 기존 테이블 삭제
DROP TABLE IF EXISTS comment_tbl;
DROP TABLE IF EXISTS board_tbl;
DROP TABLE IF EXISTS member_tbl;

-- 2. 회원 테이블 (SHA-256 암호화 적용)
CREATE TABLE member_tbl (
    id VARCHAR(50) PRIMARY KEY,
    pw VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    part VARCHAR(100)
);

-- 3. 게시판 테이블
CREATE TABLE board_tbl (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES member_tbl(id) ON DELETE SET NULL
);

-- 4. 댓글 테이블
CREATE TABLE comment_tbl (
    id INT AUTO_INCREMENT PRIMARY KEY,
    board_id INT NOT NULL,
    author_id VARCHAR(50),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES board_tbl(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES member_tbl(id) ON DELETE SET NULL
);

-- 5. 필수 초기 데이터 (총괄 관리자: admin / 비번: 1234)
INSERT INTO member_tbl VALUES ('admin', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '마스터관리자', '시스템 총괄');

-- 6. 테스트용 공지사항 (작성자: admin)
INSERT INTO board_tbl (category, title, content, author_id) VALUES 
('공지', '📌 Step-up Backend 프로젝트 안내', '시스템 초기화가 완료되었습니다. 미션 가이드에 따라 회원가입부터 시작해 보세요.', 'admin'),
('공지', '📢 비밀번호 암호화 확인 방법', '회원가입 후 MySQL에서 member_tbl을 조회하면 암호화된 값을 볼 수 있습니다.', 'admin');

SELECT * FROM member_tbl