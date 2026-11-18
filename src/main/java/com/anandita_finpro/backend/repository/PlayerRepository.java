
package com.pusaka.backend.repository;

import com.pusaka.backend.model.PlayerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PlayerRepository
        extends JpaRepository<PlayerAccount, Long> {

    Optional<PlayerAccount> findByUsername(String username);

    boolean existsByUsername(String username);
}
