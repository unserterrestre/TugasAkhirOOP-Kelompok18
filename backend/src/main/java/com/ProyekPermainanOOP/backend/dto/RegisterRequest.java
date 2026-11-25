package com.ProyekPermainanOOP.backend.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.nio.charset.StandardCharsets;

public record RegisterRequest(
        @NotBlank @Pattern(regexp = "[a-zA-Z0-9_]{3,40}") String username,
        @NotBlank @Size(min = 8, max = 72) String password
) {
    public RegisterRequest {
        if (username != null) {
            username = username.strip();
        }
    }

    @AssertTrue
    public boolean isPasswordWithinBcryptLimit() {
        return password == null || password.getBytes(StandardCharsets.UTF_8).length <= 72;
    }
}
