package com.test.dto;

public class Comment {
    private int id;           // 댓글 고유 번호
    private int boardId;      // 어떤 게시글에 달린 댓글인지 (외래키 역할)
    private String authorId;  // 댓글 작성자
    private String content;   // 댓글 내용

    public Comment(int id, int boardId, String authorId, String content) {
        this.id = id;
        this.boardId = boardId;
        this.authorId = authorId;
        this.content = content;
    }

    public int getId() { return id; }
    public int getBoardId() { return boardId; }
    public String getAuthorId() { return authorId; }
    public String getContent() { return content; }
}