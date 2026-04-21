package com.test.dto;

public class Board {
    private int id;          // 게시글 번호
    private String title;    // 게시글 제목
    private String content;  // 게시글 내용
    private String authorId; // 작성자 ID

    // 기본 생성자 (필수)
    public Board() {}

    // 데이터 초기화를 편하게 하기 위한 생성자
    public Board(int id, String title, String content, String authorId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    // --- Getter & Setter ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
}