package br.com.jrssoftware.observastack.dto;

import java.time.Instant;

public record AlertSimulationResponse(
        String status,
        String severity,
        String message,
        double threshold,
        double observedValue,
        String recommendedAction,
        Instant timestamp
) {
}
