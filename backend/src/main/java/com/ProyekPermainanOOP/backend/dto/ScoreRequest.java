package com.ProyekPermainanOOP.backend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ScoreRequest(@NotNull @Min(0) @Max(100000000) Integer score) {}
