package com.stepupbackend.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoardCreateRequest(
        @NotBlank @Size(max = 20) String category,
        @NotBlank @Size(max = 200) String title,
        @NotBlank String content) {
}
