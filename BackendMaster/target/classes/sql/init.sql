/*
  Step-up Backend: Mock-to-SQL Project 
  초기 데이터베이스 구축 스크립트
*/

-- 1. 기존 테이블 삭제 (관계 역순)
DROP TABLE IF EXISTS comment_tbl;
DROP TABLE IF EXISTS board_tbl;
DROP TABLE IF EXISTS member_tbl;

-- 2. 회원 테이블 (비밀번호는 SHA-256 해시값 저장)
CREATE TABLE member_tbl (
    id VARCHAR(50) PRIMARY KEY,
    pw VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    part VARCHAR(100)
);

-- 3. 게시판 테이블 (ON DELETE SET NULL 적용)
CREATE TABLE board_tbl (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_id VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES member_tbl(id) ON DELETE SET NULL
);

-- 4. 댓글 테이블 (ON DELETE CASCADE 적용)
CREATE TABLE comment_tbl (
    id INT AUTO_INCREMENT PRIMARY KEY,
    board_id INT NOT NULL,
    author_id VARCHAR(50),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (board_id) REFERENCES board_tbl(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES member_tbl(id) ON DELETE SET NULL
);

-- 5. 필수 초기 데이터 (관리자 비번: 1234 / 유저 비번: 1111, 2222)
INSERT INTO member_tbl VALUES ('admin', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', '마스터관리자', '시스템 총괄');
INSERT INTO member_tbl VALUES ('user01', '011c945f30ce2cbafc452f39840f025693339c42abd2454650426540c69d8065', '멤버 1', '백엔드 스터디원');
INSERT INTO member_tbl VALUES ('user02', 'edee293307936a18d1576449978394bc403622741544a044474706509426f86b', '멤버 2', 'DB 모델링 담당');

-- 6. 테스트용 공지사항 및 게시글
INSERT INTO board_tbl (category, title, content, author_id) VALUES 
('공지', '📌 프로젝트 가이드: DB 초기화 완료', 'resources/sql/init.sql 파일을 통해 구축된 환경입니다.', 'admin'),
('공지', '📢 미션 수행 전 필독사항', 'index.html에 기재된 4가지 기술 미션을 수행해보세요.', 'admin'),
('학습', '서블릿 마이그레이션 회고', 'MockDB에서 MySQL로 옮기는 과정이 유익했습니다.', 'user01');