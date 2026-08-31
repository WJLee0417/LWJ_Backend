package com.stepupbackend.dto.board;

import java.time.LocalDateTime;

import com.stepupbackend.domain.Board;

public record BoardDetailResponse(
        Long id,
        String category,
        String title,
        String content,
        String authorId,
        int views,
        LocalDateTime createdAt) {

    public static BoardDetailResponse from(Board board) {
        String authorId = board.getAuthor() == null ? null : board.getAuthor().getId();
        return new BoardDetailResponse(
                board.getId(),
                board.getCategory(),
                board.getTitle(),
                board.getContent(),
                authorId,
                board.getViews(),
                board.getCreatedAt());
    }
}
