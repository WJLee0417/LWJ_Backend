package com.test.dto;

public class Board {
    private int id;
    private String category; // 📌 추가된 카테고리 필드
    private String title;
    private String content;
    private String authorId;
    private int views;
    private String createdAt;

    // 생성자 업데이트
    public Board(int id, String category, String title, String content, String authorId, int views, String createdAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.views = views;
        this.createdAt = createdAt;
    }

    // Getter & Setter 추가
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public void setId(int id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	
	public void setViews(int views) {
		this.views = views;
	}
	
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	// (기존 id, title, content, authorId의 Getter/Setter는 그대로 유지)
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }
    public int getViews() { return views; }
    public String getCreatedAt() { return createdAt; }
}