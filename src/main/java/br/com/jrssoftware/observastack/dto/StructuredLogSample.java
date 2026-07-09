package br.com.jrssoftware.observastack.dto;

import java.time.Instant;
import java.util.Map;

public record StructuredLogSample(
        Instant timestamp,
        String level,
        String event,
        Map<String, Object> fields
) {
}
