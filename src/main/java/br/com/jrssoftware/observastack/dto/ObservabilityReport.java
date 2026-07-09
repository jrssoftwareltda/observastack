package br.com.jrssoftware.observastack.dto;

import java.time.Instant;
import java.util.List;

public record ObservabilityReport(
        String service,
        Instant timestamp,
        List<MetricSnapshot> metrics,
        List<String> operationalEndpoints,
        String dashboardPath,
        String prometheusPath
) {
}
