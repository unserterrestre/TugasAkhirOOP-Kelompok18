
package com.anandita_finpro.backend.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "game_scores")
public class GameScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private PlayerAccount player;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected GameScore() {}

    public GameScore(PlayerAccount player, int score) {
        this.player = player;
        this.score = score;
        this.createdAt = Instant.now();
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