package com.ProyekPermainanOOP.backend.service;

import com.ProyekPermainanOOP.backend.dto.RegisterRequest;
import com.ProyekPermainanOOP.backend.model.PlayerAccount;
import com.ProyekPermainanOOP.backend.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthServiceTests {
    private final PlayerRepository players = mock(PlayerRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final AuthService auth = new AuthService(players, encoder);

    @Test
    void sendsOnlyNormalizedUsernameAndEncodedPasswordToRepository() {
        String password = UUID.randomUUID().toString();
        when(encoder.encode(password)).thenReturn("encoded-value");
        when(players.saveAndFlush(any(PlayerAccount.class))).thenAnswer(call -> call.getArgument(0));

        assertThat(auth.register(new RegisterRequest(" Test_Player ", password)).username()).isEqualTo("test_player");

        ArgumentCaptor<PlayerAccount> account = ArgumentCaptor.forClass(PlayerAccount.class);
        verify(players).saveAndFlush(account.capture());
        assertThat(account.getValue().getUsername()).isEqualTo("test_player");
        assertThat(account.getValue().getPasswordHash()).isEqualTo("encoded-value");
    }

    @Test
    void duplicateDoesNotEncodeOrSave() {
        when(players.existsByUsername("taken")).thenReturn(true);

        assertThatThrownBy(() -> auth.register(new RegisterRequest("TAKEN", UUID.randomUUID().toString())))
                .isInstanceOf(ResponseStatusException.class);
        verifyNoInteractions(encoder);
        verify(players, never()).saveAndFlush(any(PlayerAccount.class));
    }
}
