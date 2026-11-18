
package com.pusaka.backend.repository;

import com.pusaka.backend.model.GameScore;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GameScoreRepository
        extends JpaRepository<GameScore, Long> {

    List<GameScore> findTop10ByPlayerUsernameOrderByCreatedAtDescIdDesc(
            String username);
}
