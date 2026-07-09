package br.com.jrssoftware.observastack.dto;

import java.time.Instant;

public record TrafficSimulationResponse(
        int requestedOperations,
        int successfulOperations,
        int failedOperations,
        boolean optimizedPath,
        double averageLatencyMillis,
        String logFormat,
        Instant timestamp
) {
}
