package com.stepupbackend.dto.board;

/** Query parameters normalized and validated by BoardService before repository access. */
public record BoardSearchRequest(String category, String searchType, String keyword, int page, int size) {
}
