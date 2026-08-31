package com.stepupbackend.dto.member;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Input DTO for the future member registration form. */
public record MemberSignupRequest(
        @NotBlank @Size(max = 50) String id,
        @NotBlank String password,
        @NotBlank @Size(max = 50) String name,
        @Size(max = 100) String part) {
}
