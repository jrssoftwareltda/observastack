package br.com.jrssoftware.observastack.dto;

import java.time.Instant;

public record OptimizationComparisonResponse(
        String scenario,
        SearchMeasurement before,
        SearchMeasurement after,
        double improvementPercent,
        String businessMeaning,
        Instant timestamp
) {
}
