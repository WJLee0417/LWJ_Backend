package com.stepupbackend.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentCreateRequest(@NotBlank String content) {
}
