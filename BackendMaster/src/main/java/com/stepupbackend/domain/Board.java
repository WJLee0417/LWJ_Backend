package com.stepupbackend.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "board_tbl")
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "category", nullable = false, length = 20)
    private String category;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    /** A deleted member leaves the board intact with a null author_id. */
    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    @Column(name = "views", nullable = false)
    private int views = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** JPA removal mirrors the database FK's ON DELETE CASCADE policy. */
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    protected Board() {
    }

    public Board(String category, String title, String content, Member author) {
        this.category = category;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    @PrePersist
    private void initializeDefaults() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public void incrementViews() {
        views++;
    }

    public void update(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public boolean isWrittenBy(String memberId) {
        return author != null && author.getId().equals(memberId);
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Member getAuthor() {
        return author;
    }

    public int getViews() {
        return views;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
