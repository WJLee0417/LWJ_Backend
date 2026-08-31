package com.stepupbackend.dto.board;

import java.util.List;

public record BoardPageResponse(
        List<BoardSummaryResponse> notices,
        List<BoardSummaryResponse> boards,
        int page,
        int size,
        int totalPages,
        long totalElements) {
}
