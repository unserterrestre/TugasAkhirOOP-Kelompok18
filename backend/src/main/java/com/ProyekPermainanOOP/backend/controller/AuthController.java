package com.ProyekPermainanOOP.backend.controller;

import com.ProyekPermainanOOP.backend.dto.AccountResponse;
import com.ProyekPermainanOOP.backend.dto.RegisterRequest;
import com.ProyekPermainanOOP.backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth;

    public AuthController(AuthService auth) {
        this.auth = auth;
    }

    @PostMapping(path = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AccountResponse register(@Valid @RequestBody RegisterRequest request) {
        return auth.register(request);
    }

    @GetMapping("/me")
    public AccountResponse me(Principal principal) {
        return auth.currentAccount(principal.getName());
    }
}
