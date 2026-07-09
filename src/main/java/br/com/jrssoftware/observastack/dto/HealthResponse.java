package br.com.jrssoftware.observastack.dto;

import java.time.Instant;
import java.util.List;

public record HealthResponse(
        String status,
        String service,
        String version,
        Instant timestamp,
        List<DependencyStatus> dependencies
) {
}
