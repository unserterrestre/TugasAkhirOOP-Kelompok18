package com.ProyekPermainanOOP.backend;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import tools.jackson.databind.JsonNode;

import java.nio.file.Path;

import static com.ProyekPermainanOOP.backend.BackendApplicationTests.*;
import static org.assertj.core.api.Assertions.assertThat;

class FileDatabaseRestartTests {
    @TempDir
    Path databaseDirectory;

    @Test
    void restoresAccountAndScoreAfterFullContextRestart() throws Exception {
        String database = "jdbc:h2:file:" + databaseDirectory.resolve("restart").toString().replace('\\', '/');
        String auth = basic("restart_player");
        JsonNode saved;
        try (ConfigurableApplicationContext first = start(database)) {
            int port = port(first);
            expect(201, register(port, "restart_player"));
            saved = json(expect(201, request(port, "/api/scores", "{\"score\":178}", auth, null)));
        }

        assertThat(databaseDirectory.resolve("restart.mv.db")).exists();
        try (ConfigurableApplicationContext second = start(database)) {
            int port = port(second);
            expect(200, request(port, "/api/auth/me", null, auth, null));
            assertThat(json(expect(200, request(port, "/api/scores/mine", null, auth, null))))
                    .containsExactly(saved);
        }
    }

    private ConfigurableApplicationContext start(String database) {
        return new SpringApplicationBuilder(BackendApplication.class).run(
                "--server.port=0",
                "--spring.datasource.url=" + database + ";DB_CLOSE_ON_EXIT=FALSE",
                "--spring.jpa.hibernate.ddl-auto=update");
    }

    private int port(ConfigurableApplicationContext context) {
        return ((WebServerApplicationContext) context).getWebServer().getPort();
    }
}
