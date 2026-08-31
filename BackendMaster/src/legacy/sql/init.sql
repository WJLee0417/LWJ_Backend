/*
  Step-up Backend: Mock-to-SQL Project
  초기 데이터베이스 구축 스크립트
*/

-- 1. 기존 테이블 삭제
DROP TABLE IF EXISTS comment_tbl;
DROP TABLE IF EXISTS board_tbl;
DROP TABLE IF EXISTS member_tbl;

-- 2. 회원 테이블 (BCrypt password hash 저장)
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
    views INT NOT NULL DEFAULT 0,
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

-- 5. 관리자 계정은 애플리케이션 시작 시 AppInitListener가 BCrypt 해시로 생성한다.

-- 6. 테스트용 공지사항은 관리자 계정 생성 후 별도로 추가한다.
INSERT INTO board_tbl (category, title, content, author_id) VALUES 
('공지', '📌 Step-up Backend 프로젝트 안내', '시스템 초기화가 완료되었습니다. 미션 가이드에 따라 회원가입부터 시작해 보세요.', NULL),
('공지', '📢 비밀번호 해싱 확인 방법', '회원가입 후 MySQL에서 member_tbl을 조회하면 BCrypt 해시값을 볼 수 있습니다.', NULL);
