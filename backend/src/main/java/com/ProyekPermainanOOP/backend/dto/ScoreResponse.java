package com.ProyekPermainanOOP.backend.dto;

import java.time.Instant;

public record ScoreResponse(Long id, int score, Instant createdAt) {}
