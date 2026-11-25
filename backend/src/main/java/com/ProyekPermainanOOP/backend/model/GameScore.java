package com.ProyekPermainanOOP.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Table(name = "game_scores", uniqueConstraints =
        @UniqueConstraint(name = "uk_score_player_request", columnNames = {"player_id", "request_id"}))
public class GameScore {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerAccount player;

    @Min(0)
    @Max(100000000)
    @Column(nullable = false)
    private int score;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "request_id", updatable = false)
    private UUID requestId;

    protected GameScore() {}

    public GameScore(PlayerAccount player, int score, UUID requestId) {
        this.player = player;
        this.score = score;
        this.requestId = requestId;
        this.createdAt = Instant.now().truncatedTo(ChronoUnit.MICROS);
    }

    public Long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
