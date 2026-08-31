package com.test.dto;

public class Board {
    private int id;
    private String category;
    private String title;
    private String content;
    private String authorId;
    private int views;
    private String createdAt;

    public Board(int id, String category, String title, String content, String authorId, int views, String createdAt) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.views = views;
        this.createdAt = createdAt;
    }

    /** Retained only for the deprecated MockDB fixture. */
    public Board(int id, String category, String title, String content, String authorId) {
        this(id, category, title, content, authorId, 0, null);
    }

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

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getAuthorId() { return authorId; }
    public int getViews() { return views; }
    public String getCreatedAt() { return createdAt; }
}
