
package com.anandita_finpro.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record ScoreRequest(
        @Min(0)
        @Max(100000000)
        int score
) {}
