package com.ProyekPermainanOOP.backend.repository;

import com.ProyekPermainanOOP.backend.model.GameScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GameScoreRepository extends JpaRepository<GameScore, Long> {
    List<GameScore> findByPlayerUsernameOrderByCreatedAtDescIdDesc(String username);
    Optional<GameScore> findFirstByPlayerUsernameOrderByCreatedAtDescIdDesc(String username);
    Optional<GameScore> findByPlayerIdAndRequestId(Long playerId, UUID requestId);
}
