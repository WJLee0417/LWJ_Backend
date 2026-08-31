CREATE TABLE member_tbl (
    id VARCHAR(50) PRIMARY KEY,
    pw VARCHAR(255) NOT NULL,
    name VARCHAR(50) NOT NULL,
    part VARCHAR(100)
);

CREATE TABLE board_tbl (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(20) NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    author_id VARCHAR(50),
    views INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_board_author
        FOREIGN KEY (author_id) REFERENCES member_tbl(id) ON DELETE SET NULL
);

CREATE TABLE comment_tbl (
    id INT AUTO_INCREMENT PRIMARY KEY,
    board_id INT NOT NULL,
    author_id VARCHAR(50),
    content TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_comment_board
        FOREIGN KEY (board_id) REFERENCES board_tbl(id) ON DELETE CASCADE,
    CONSTRAINT fk_comment_author
        FOREIGN KEY (author_id) REFERENCES member_tbl(id) ON DELETE SET NULL
);

INSERT INTO board_tbl (category, title, content, author_id) VALUES
    ('공지', 'Step-up Backend 프로젝트 안내', '시스템 초기화가 완료되었습니다. 회원가입부터 시작해 보세요.', NULL),
    ('공지', '비밀번호 해싱 확인 방법', '회원가입 후 member_tbl에서 BCrypt 해시를 확인할 수 있습니다.', NULL);
