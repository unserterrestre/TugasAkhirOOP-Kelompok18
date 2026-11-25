package com.ProyekPermainanOOP.backend.service;

import com.ProyekPermainanOOP.backend.dto.ScoreResponse;
import com.ProyekPermainanOOP.backend.model.GameScore;
import com.ProyekPermainanOOP.backend.model.PlayerAccount;
import com.ProyekPermainanOOP.backend.repository.GameScoreRepository;
import com.ProyekPermainanOOP.backend.repository.PlayerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScoreService {
    private final PlayerRepository players;
    private final GameScoreRepository scores;

    public ScoreService(PlayerRepository players, GameScoreRepository scores) {
        this.players = players;
        this.scores = scores;
    }

    @Transactional
    public SavedScore save(String username, int score, UUID requestId) {
        PlayerAccount player = players.findForScoreSave(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account unavailable"));

        if (requestId != null) {
            Optional<GameScore> previous = scores.findByPlayerIdAndRequestId(player.getId(), requestId);
            if (previous.isPresent()) {
                GameScore saved = previous.get();
                if (saved.getScore() != score) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Idempotency-Key already used for a different score.");
                }
                return new SavedScore(response(saved), false);
            }
        }

        GameScore saved = scores.saveAndFlush(new GameScore(player, score, requestId));
        return new SavedScore(response(saved), true);
    }

    @Transactional(readOnly = true)
    public List<ScoreResponse> history(String username) {
        return scores.findByPlayerUsernameOrderByCreatedAtDescIdDesc(username).stream()
                .map(ScoreService::response).toList();
    }

    @Transactional(readOnly = true)
    public Optional<ScoreResponse> latest(String username) {
        return scores.findFirstByPlayerUsernameOrderByCreatedAtDescIdDesc(username)
                .map(ScoreService::response);
    }

    private static ScoreResponse response(GameScore score) {
        return new ScoreResponse(score.getId(), score.getScore(), score.getCreatedAt());
    }

    public record SavedScore(ScoreResponse score, boolean created) {}
}
