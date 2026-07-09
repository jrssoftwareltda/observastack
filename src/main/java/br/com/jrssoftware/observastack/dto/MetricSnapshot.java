package br.com.jrssoftware.observastack.dto;

public record MetricSnapshot(
        String name,
        double value,
        String unit,
        String description
) {
}
