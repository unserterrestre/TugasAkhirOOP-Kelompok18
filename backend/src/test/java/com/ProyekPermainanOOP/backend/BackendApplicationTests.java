package com.ProyekPermainanOOP.backend;

import com.ProyekPermainanOOP.backend.repository.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "spring.datasource.url=jdbc:h2:mem:pusaka_api_tests",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class BackendApplicationTests {
    private static final HttpClient CLIENT = HttpClient.newHttpClient();
    private static final String PASSWORD = UUID.randomUUID().toString();

    @LocalServerPort
    int port;

    @Autowired
    PlayerRepository players;

    @Autowired
    PasswordEncoder encoder;

    @Test
    void registersNormalizesAndHashesPassword() throws Exception {
        JsonNode account = json(expect(201, register(port, "  Reg_Player  ")));
        assertThat(account.get("username").asString()).isEqualTo("reg_player");
        assertThat(account.get("id").longValue()).isPositive();
        assertThat(account.size()).isEqualTo(2);
        String stored = players.findByUsername("reg_player").orElseThrow().getPasswordHash();
        assertThat(stored).isNotEqualTo(PASSWORD);
        assertThat(encoder.matches(PASSWORD, stored)).isTrue();
    }

    @Test
    void rejectsDuplicateNormalizedUsername() throws Exception {
        expect(201, register(port, "duplicate_player"));
        assertThat(json(expect(409, register(port, " DUPLICATE_PLAYER "))).get("code").asString())
                .isEqualTo("conflict");
    }

    @Test
    void rejectsInvalidRegistration() throws Exception {
        String[] invalid = {
                """
                {"username":"bad name","password":"valid-length"}
                """,
                """
                {"username":"valid_name","password":"short"}
                """,
                "{\"username\":\"valid_name\",\"password\":\"" + "é".repeat(37) + "\"}",
                "{\"username\":\"valid_name\"}",
                "{"
        };
        for (String body : invalid) {
            JsonNode error = json(expect(400, request(port, "/api/auth/register", body, null, null)));
            assertThat(error.get("code").asString()).isEqualTo("invalid_request");
        }
    }

    @Test
    void acceptsValidBasicCredentialsAndRejectsInvalidCredentials() throws Exception {
        expect(201, register(port, "login_player"));
        expect(200, request(port, "/api/auth/me", null, basic("LOGIN_PLAYER", PASSWORD), null));
        HttpResponse<String> failure = request(port, "/api/auth/me", null, basic("login_player", "incorrect"), null);
        assertThat(json(expect(401, failure)).get("code").asString()).isEqualTo("unauthorized");
        assertThat(failure.headers().firstValue("WWW-Authenticate")).hasValueSatisfying(
                value -> assertThat(value).startsWith("Basic "));
    }

    @Test
    void rejectsUnauthenticatedScoreRequests() throws Exception {
        expect(401, request(port, "/api/scores", "{\"score\":0}", null, null));
        expect(401, request(port, "/api/scores/mine", null, null, null));
        expect(401, request(port, "/api/scores/mine/latest", null, null, null));
    }

    @Test
    void rejectsInvalidScores() throws Exception {
        expect(201, register(port, "validation_player"));
        String auth = basic("validation_player", PASSWORD);
        for (String body : new String[] {"{\"score\":-1}", "{\"score\":100000001}", "{}", "{\"score\":null}",
                "{\"score\":1.5}"}) {
            expect(400, request(port, "/api/scores", body, auth, null));
        }
        expect(400, request(port, "/api/scores", "{\"score\":1}", auth, "invalid-key"));
        assertThat(json(expect(200, request(port, "/api/scores/mine", null, auth, null))).size()).isZero();
    }

    @Test
    void storesScoresUnderAuthenticatedPlayerAndReturnsLatestByTime() throws Exception {
        expect(201, register(port, "history_alice"));
        expect(201, register(port, "history_bob"));
        String alice = basic("history_alice", PASSWORD);
        String bob = basic("history_bob", PASSWORD);

        expect(204, request(port, "/api/scores/mine/latest", null, alice, null));
        JsonNode first = json(expect(201, request(port, "/api/scores", "{\"score\":1500}", alice, null)));
        JsonNode latest = json(expect(201, request(port, "/api/scores",
                "{\"score\":12,\"username\":\"history_bob\"}", alice, null)));
        JsonNode other = json(expect(201, request(port, "/api/scores", "{\"score\":33}", bob, null)));

        assertThat(latest.get("score").intValue()).isEqualTo(12);
        assertThat(latest.get("id").longValue()).isPositive();
        assertThat(latest.get("createdAt").asString()).isNotBlank();
        assertThat(latest.size()).isEqualTo(3);
        assertThat(json(expect(200, request(port, "/api/scores/mine/latest", null, alice, null))))
                .isEqualTo(latest);
        JsonNode aliceScores = json(expect(200, request(port, "/api/scores/mine", null, alice, null)));
        assertThat(aliceScores).containsExactly(latest, first);
        JsonNode bobScores = json(expect(200, request(port, "/api/scores/mine", null, bob, null)));
        assertThat(bobScores).containsExactly(other);
    }

    @Test
    void concurrentRetriesKeepOneScoreAndRejectKeyReuseWithDifferentValue() throws Exception {
        expect(201, register(port, "retry_player"));
        String auth = basic("retry_player", PASSWORD);
        String key = UUID.randomUUID().toString();
        String body = "{\"score\":5}";
        CompletableFuture<HttpResponse<String>> first = CLIENT.sendAsync(
                buildRequest(port, "/api/scores", body, auth, key), HttpResponse.BodyHandlers.ofString());
        HttpResponse<String> second = request(port, "/api/scores", body, auth, key);
        assertThat(new int[] {first.get().statusCode(), second.statusCode()}).containsExactlyInAnyOrder(200, 201);
        assertThat(json(first.get().body())).isEqualTo(json(second.body()));
        expect(409, request(port, "/api/scores", "{\"score\":6}", auth, key));
        assertThat(json(expect(200, request(port, "/api/scores/mine", null, auth, null)))).hasSize(1);
    }

    static HttpResponse<String> register(int port, String username) throws Exception {
        return request(port, "/api/auth/register", """
                {"username":"%s","password":"%s"}
                """.formatted(username, PASSWORD), null, null);
    }

    static String basic(String username) {
        return basic(username, PASSWORD);
    }

    private static String basic(String username, String password) {
        return "Basic " + Base64.getEncoder().encodeToString(
                (username + ":" + password).getBytes(StandardCharsets.UTF_8));
    }

    static HttpResponse<String> request(int port, String path, String body, String auth, String key)
            throws Exception {
        return CLIENT.send(buildRequest(port, path, body, auth, key),
                HttpResponse.BodyHandlers.ofString());
    }

    private static HttpRequest buildRequest(int port, String path, String body, String auth, String key) {
        HttpRequest.Builder request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json");
        if (auth != null) {
            request.header("Authorization", auth);
        }
        if (key != null) {
            request.header("Idempotency-Key", key);
        }
        if (body != null) {
            request.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body));
        }
        return request.build();
    }

    static String expect(int status, HttpResponse<String> response) {
        assertThat(response.statusCode()).as("response body: %s", response.body()).isEqualTo(status);
        return response.body();
    }

    static JsonNode json(String body) {
        return JsonMapper.shared().readTree(body);
    }

}
