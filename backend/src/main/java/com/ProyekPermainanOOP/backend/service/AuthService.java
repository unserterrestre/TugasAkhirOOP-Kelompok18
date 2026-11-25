package com.ProyekPermainanOOP.backend.service;

import com.ProyekPermainanOOP.backend.dto.AccountResponse;
import com.ProyekPermainanOOP.backend.dto.RegisterRequest;
import com.ProyekPermainanOOP.backend.model.PlayerAccount;
import com.ProyekPermainanOOP.backend.repository.PlayerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Locale;

@Service
public class AuthService implements UserDetailsService {
    private final PlayerRepository players;
    private final PasswordEncoder passwordEncoder;

    public AuthService(PlayerRepository players, PasswordEncoder passwordEncoder) {
        this.players = players;
        this.passwordEncoder = passwordEncoder;
    }

    public AccountResponse register(RegisterRequest request) {
        String username = normalize(request.username());
        if (players.existsByUsername(username)) {
            throw duplicateUsername();
        }

        PlayerAccount account = new PlayerAccount(username, passwordEncoder.encode(request.password()));
        try {
            return response(players.saveAndFlush(account));
        } catch (DataIntegrityViolationException failure) {
            if (players.existsByUsername(username)) {
                throw duplicateUsername();
            }
            throw failure;
        }
    }

    public AccountResponse currentAccount(String username) {
        return response(findAccount(username));
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        PlayerAccount account = findAccount(normalize(username));
        return User.withUsername(account.getUsername())
                .password(account.getPasswordHash())
                .roles("PLAYER")
                .build();
    }

    private PlayerAccount findAccount(String username) {
        return players.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Account unavailable"));
    }

    private static String normalize(String username) {
        return username.strip().toLowerCase(Locale.ROOT);
    }

    private static AccountResponse response(PlayerAccount account) {
        return new AccountResponse(account.getId(), account.getUsername());
    }

    private static ResponseStatusException duplicateUsername() {
        return new ResponseStatusException(HttpStatus.CONFLICT, "Username already registered.");
    }
}
