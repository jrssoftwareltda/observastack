package br.com.jrssoftware.observastack.controller;

import br.com.jrssoftware.observastack.dto.DependencyStatus;
import br.com.jrssoftware.observastack.dto.HealthResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

@RestController
public class HealthController {

    private final String version;

    public HealthController(@Value("${info.app.version:0.1.0}") String version) {
        this.version = version;
    }

    @GetMapping("/health")
    public HealthResponse health() {
        return new HealthResponse(
                "UP",
                "ObservaStack",
                version,
                Instant.now(),
                List.of(
                        new DependencyStatus("api", "UP", "Spring Boot application is accepting requests"),
                        new DependencyStatus("metrics", "UP", "Micrometer registry is available"),
                        new DependencyStatus("logs", "UP", "Structured JSON logging is enabled"),
                        new DependencyStatus("dashboard", "UP", "Static dashboard is available at /"),
                        new DependencyStatus("database", "SIMULATED", "This case uses an in-memory dataset to keep setup fast")
                )
        );
    }
}
