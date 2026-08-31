package com.stepupbackend.dto.comment;

import java.time.LocalDateTime;

import com.stepupbackend.domain.Comment;

/** Read DTO for comments shown with a board detail page. */
public record CommentResponse(Long id, String authorId, String content, LocalDateTime createdAt) {

    public static CommentResponse from(Comment comment) {
        String authorId = comment.getAuthor() == null ? null : comment.getAuthor().getId();
        return new CommentResponse(comment.getId(), authorId, comment.getContent(), comment.getCreatedAt());
    }
}
