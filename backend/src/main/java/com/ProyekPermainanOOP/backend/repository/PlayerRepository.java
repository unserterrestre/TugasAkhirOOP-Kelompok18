package com.ProyekPermainanOOP.backend.repository;

import com.ProyekPermainanOOP.backend.model.PlayerAccount;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<PlayerAccount, Long> {
    Optional<PlayerAccount> findByUsername(String username);
    boolean existsByUsername(String username);

    // Serialize submissions from the same player so retries see the committed score.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select player from PlayerAccount player where player.username = :username")
    Optional<PlayerAccount> findForScoreSave(@Param("username") String username);
}
