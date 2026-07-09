package br.com.jrssoftware.observastack.dto;

public record SearchMeasurement(
        String mode,
        long durationMillis,
        int resultCount,
        String strategy
) {
}
