package com.ProyekPermainanOOP.backend.controller;

import com.ProyekPermainanOOP.backend.dto.ScoreRequest;
import com.ProyekPermainanOOP.backend.dto.ScoreResponse;
import com.ProyekPermainanOOP.backend.service.ScoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/scores")
public class ScoreController {
    private final ScoreService scores;

    public ScoreController(ScoreService scores) {
        this.scores = scores;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ScoreResponse> save(Principal principal, @Valid @RequestBody ScoreRequest request,
            @RequestHeader(name = "Idempotency-Key", required = false) UUID requestId) {
        ScoreService.SavedScore saved = scores.save(principal.getName(), request.score(), requestId);
        return ResponseEntity.status(saved.created() ? HttpStatus.CREATED : HttpStatus.OK).body(saved.score());
    }

    @GetMapping("/mine")
    public List<ScoreResponse> mine(Principal principal) {
        return scores.history(principal.getName());
    }

    @GetMapping("/mine/latest")
    public ResponseEntity<ScoreResponse> latest(Principal principal) {
        return scores.latest(principal.getName())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}
