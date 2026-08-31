package com.stepupbackend.dto.board;

import java.time.LocalDateTime;

import com.stepupbackend.domain.Board;

/** Read DTO that keeps persistence entities out of view and web layers. */
public record BoardSummaryResponse(
        Long id,
        String category,
        String title,
        String authorId,
        int views,
        LocalDateTime createdAt) {

    public static BoardSummaryResponse from(Board board) {
        String authorId = board.getAuthor() == null ? null : board.getAuthor().getId();
        return new BoardSummaryResponse(
                board.getId(),
                board.getCategory(),
                board.getTitle(),
                authorId,
                board.getViews(),
                board.getCreatedAt());
    }
}
