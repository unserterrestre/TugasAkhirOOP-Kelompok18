# Pusaka Mahjong backend and desktop API

The Spring Boot backend registers player accounts, authenticates desktop requests, and stores each player's score history in a local H2 database.

## Run locally

Run these commands from the `backend` directory with a Java 17 JDK. If the wrapper reports an invalid `JAVA_HOME`, set it to the JDK installation directory for the current terminal.

```powershell
.\gradlew.bat clean compileJava
.\gradlew.bat test
.\gradlew.bat bootRun
```

The default base URL is `http://127.0.0.1:8080`. The server binds to loopback. With `backend` as the working directory, the persistent database is `backend/data/pusakadb.mv.db`. Hibernate updates the schema on startup, and the H2 Console is disabled.

## Authentication and headers

Use HTTP Basic with `GET /api/auth/me` to check the username and password. Every private request sends `Authorization: Basic <base64 of username:password encoded as UTF-8>`. Registration is public and should omit this header. Send `Accept: application/json` and include `Content-Type: application/json` for POST bodies.

Use this HTTP Basic setup over loopback for local development. Keep credentials in desktop process memory for the signed-in session and clear them at logout. Treat the Basic header as a password and exclude it from application logs and saved configuration. Public deployment requires HTTPS and a suitable authentication or session mechanism. Credentials travel with each authenticated request.

## API endpoints

| Method and path | Authentication | Successful response |
| --- | --- | --- |
| `POST /api/auth/register` | Public | `201 Created` with an account |
| `GET /api/auth/me` | Basic | `200 OK` with the authenticated account |
| `POST /api/scores` | Basic | `201 Created` with a new score, or `200 OK` when replaying a saved request |
| `GET /api/scores/mine` | Basic | `200 OK` with a newest-first JSON array, or `[]` for an empty history |
| `GET /api/scores/mine/latest` | Basic | `200 OK` with the most recent score, or `204 No Content` with an empty body |

Registration body. Substitute the password entered by the player for the example value.

```json
{
  "username": "pemain_demo",
  "password": "<player-entered password>"
}
```

Usernames are trimmed, lowercased, and unique. They must contain 3–40 ASCII letters, digits, or underscores after trimming. Passwords must be nonblank and at least 8 Java string characters, with a limit of 72 UTF-8 bytes for BCrypt. Registration and `/me` return the same account shape.

```json
{"id": 1, "username": "pemain_demo"}
```

Submit a score as a JSON integer from 0 to 100,000,000 inclusive. The backend takes ownership from the authenticated principal. Client-submitted scores are accepted without server-authoritative gameplay verification.

```json
{"score": 1500}
```

For each game session, generate one UUID and send it in the optional `Idempotency-Key` header. Reuse that UUID and the same score if a save times out or the response is lost. A successful replay returns the original score with `200`. Reusing the same key for a different score on the same account returns `409`. Omitting the header creates a new record with each valid POST.

A score object contains a numeric ID, the score, and the UTC creation time in ISO 8601 format. The save and latest endpoints return one object. The history endpoint wraps zero or more score objects in a JSON array. Scores with equal timestamps are ordered by descending ID.

```json
{"id": 12, "score": 1500, "createdAt": "2026-11-25T01:20:30.123456Z"}
```

```json
[
  {"id": 12, "score": 1500, "createdAt": "2026-11-25T01:20:30.123456Z"},
  {"id": 11, "score": 2500, "createdAt": "2026-11-25T01:05:00Z"}
]
```

Expected API errors use a JSON code and a short message.

```json
{"code": "unauthorized", "message": "Valid credentials are required."}
```

| Status | Code | Desktop handling |
| --- | --- | --- |
| `400 Bad Request` | `invalid_request` | Show validation feedback. Correct missing fields, invalid JSON, score limits, or an invalid idempotency header before retrying. |
| `401 Unauthorized` | `unauthorized` | Prompt for credentials again. Private requests require a valid Basic header. |
| `409 Conflict` | `conflict` | Ask for another username on registration. For a score, check whether the same UUID was reused with a different value. |
| `415 Unsupported Media Type` | `unsupported_media_type` | Send POST bodies with `Content-Type: application/json`. |
| `503 Service Unavailable` | `database_unavailable` | Preserve the pending score and retry later with the original UUID. |

## LibGDX integration

1. Put account requests in `AuthApiClient` and score requests in `ScoreApiClient`. Let `BackendFacade` own their shared base URL and in-memory signed-in state.
2. On sign-in, call `GET /api/auth/me`. Once it returns `200`, load `GET /api/scores/mine` or use the latest endpoint and handle an empty `204` response.
3. Create the session UUID at game start and let `ScoreManager` calculate the score locally. On Game Over or controlled Exit, capture the final score once and submit it with the session UUID.
4. Send requests asynchronously with bounded connection and request timeouts. Use `Gdx.app.postRunnable` to apply callbacks that touch screen state. For controlled Exit, keep the screen responsive while the write is pending and allow exit if the timeout expires.
5. If the backend is unavailable, show that the score is pending or unsaved and keep the local score and UUID for retry. A retry belongs to the same account and uses the original payload. Mark the score saved only after receiving `201` or a replay `200`.

## Verification

On Windows with Java 17, Spring Boot 4.1.2, and Gradle 9.9.1, `gradlew.bat --offline clean compileJava test` passed all 11 tests. The API suite uses in-memory H2. The restart test runs two Spring contexts against a separate JUnit temporary H2 file. The unit tests mock the account repository and password encoder.

A separate `bootRun` smoke test bound to `127.0.0.1:18080` and used an isolated H2 file under `build/`. Registration returned `201`, valid and invalid authentication returned `200` and `401`, anonymous score submission returned `401`, and an authenticated save and its replay returned `201` and `200`. The second account's history was empty. A fresh backend process authenticated the original account and returned the identical saved score from the same H2 file.
