package br.com.jrssoftware.observastack.service;

import br.com.jrssoftware.observastack.config.MetricsConfig;
import br.com.jrssoftware.observastack.dto.AlertSimulationResponse;
import br.com.jrssoftware.observastack.dto.MetricSnapshot;
import br.com.jrssoftware.observastack.dto.ObservabilityReport;
import br.com.jrssoftware.observastack.dto.SearchMeasurement;
import br.com.jrssoftware.observastack.dto.StructuredLogSample;
import br.com.jrssoftware.observastack.dto.TrafficSimulationResponse;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ObservabilityService {

    private static final Logger logger = LoggerFactory.getLogger(ObservabilityService.class);

    private final MeterRegistry meterRegistry;
    private final InventorySearchService inventorySearchService;
    private final MetricsConfig metricsConfig;
    private final Random random = new Random(42);
    private final double alertLatencyThresholdMs;
    private final double alertErrorRateThreshold;

    public ObservabilityService(
            MeterRegistry meterRegistry,
            InventorySearchService inventorySearchService,
            MetricsConfig metricsConfig,
            @Value("${observastack.alerts.latency-threshold-ms:350}") double alertLatencyThresholdMs,
            @Value("${observastack.alerts.error-rate-threshold:0.05}") double alertErrorRateThreshold
    ) {
        this.meterRegistry = meterRegistry;
        this.inventorySearchService = inventorySearchService;
        this.metricsConfig = metricsConfig;
        this.alertLatencyThresholdMs = alertLatencyThresholdMs;
        this.alertErrorRateThreshold = alertErrorRateThreshold;
    }

    public TrafficSimulationResponse simulateTraffic(int requests, boolean optimized) {
        int normalizedRequests = Math.max(1, Math.min(requests, 500));
        int success = 0;
        int failures = 0;
        long totalLatencyMs = 0;

        for (int i = 0; i < normalizedRequests; i++) {
            long start = System.nanoTime();
            boolean failed = random.nextDouble() < (optimized ? 0.01 : 0.04);

            if (failed) {
                failures++;
                incrementCounter("observastack_api_errors_total", "endpoint", "traffic-simulator");
            } else {
                SearchMeasurement measurement = optimized
                        ? inventorySearchService.optimizedSearch("tenant-42")
                        : inventorySearchService.unoptimizedSearch("tenant-42");
                totalLatencyMs += measurement.durationMillis();
                success++;
            }

            long durationNs = System.nanoTime() - start;
            Timer.builder("observastack_api_latency")
                    .description("API latency measured by ObservaStack")
                    .tag("endpoint", "traffic-simulator")
                    .tag("optimized", String.valueOf(optimized))
                    .register(meterRegistry)
                    .record(durationNs, TimeUnit.NANOSECONDS);

            Counter.builder("observastack_api_requests_total")
                    .description("Total API operations observed by ObservaStack")
                    .tag("endpoint", "traffic-simulator")
                    .tag("optimized", String.valueOf(optimized))
                    .register(meterRegistry)
                    .increment();

            logger.atInfo()
                    .addKeyValue("event", "api_request_observed")
                    .addKeyValue("endpoint", "traffic-simulator")
                    .addKeyValue("optimized", optimized)
                    .addKeyValue("success", !failed)
                    .addKeyValue("durationMs", Duration.ofNanos(durationNs).toMillis())
                    .log("Observed API request");
        }

        double averageLatency = success == 0 ? 0 : (double) totalLatencyMs / success;

        return new TrafficSimulationResponse(
                normalizedRequests,
                success,
                failures,
                optimized,
                Math.round(averageLatency * 100.0) / 100.0,
                "JSON structured logs with event, endpoint, optimized, success and durationMs fields",
                Instant.now()
        );
    }

    public AlertSimulationResponse simulateAlert(String type) {
        String normalizedType = type == null ? "latency" : type.toLowerCase();

        if ("error-rate".equals(normalizedType)) {
            double observed = 0.12;
            boolean active = observed > alertErrorRateThreshold;
            metricsConfig.activeAlerts().set(active ? 1 : 0);
            logger.atWarn()
                    .addKeyValue("event", "alert_simulated")
                    .addKeyValue("type", "error-rate")
                    .addKeyValue("threshold", alertErrorRateThreshold)
                    .addKeyValue("observed", observed)
                    .log("Simulated error-rate alert");
            return new AlertSimulationResponse(
                    active ? "TRIGGERED" : "OK",
                    active ? "HIGH" : "LOW",
                    "Simulated error rate is above the configured threshold.",
                    alertErrorRateThreshold,
                    observed,
                    "Inspect recent deploys, external dependencies and API error logs.",
                    Instant.now()
            );
        }

        double observed = 520;
        boolean active = observed > alertLatencyThresholdMs;
        metricsConfig.activeAlerts().set(active ? 1 : 0);
        logger.atWarn()
                .addKeyValue("event", "alert_simulated")
                .addKeyValue("type", "latency")
                .addKeyValue("thresholdMs", alertLatencyThresholdMs)
                .addKeyValue("observedMs", observed)
                .log("Simulated latency alert");
        return new AlertSimulationResponse(
                active ? "TRIGGERED" : "OK",
                active ? "MEDIUM" : "LOW",
                "Simulated API latency is above the configured threshold.",
                alertLatencyThresholdMs,
                observed,
                "Check p95 latency, slow endpoints, database indexes and cache hit ratio.",
                Instant.now()
        );
    }

    public ObservabilityReport report() {
        return new ObservabilityReport(
                "ObservaStack",
                Instant.now(),
                List.of(
                        metric("observastack_api_requests_total", "requests", "Observed API requests"),
                        metric("observastack_api_errors_total", "errors", "Observed API errors"),
                        metric("observastack_api_latency", "seconds", "Observed API latency timer count"),
                        new MetricSnapshot("observastack_active_alerts", metricsConfig.activeAlerts().get(), "alerts", "Currently active simulated alerts")
                ),
                List.of(
                        "GET /health",
                        "GET /api/v1/observability/report",
                        "POST /api/v1/observability/traffic?requests=50&optimized=true",
                        "POST /api/v1/observability/alerts/simulate?type=latency",
                        "GET /api/v1/observability/optimization/compare",
                        "GET /actuator/prometheus"
                ),
                "/",
                "/actuator/prometheus"
        );
    }

    public StructuredLogSample structuredLogSample() {
        return new StructuredLogSample(
                Instant.now(),
                "INFO",
                "api_request_observed",
                Map.of(
                        "service", "ObservaStack",
                        "endpoint", "traffic-simulator",
                        "optimized", true,
                        "success", true,
                        "durationMs", 18,
                        "traceHint", "Use structured fields to filter production incidents quickly"
                )
        );
    }

    private MetricSnapshot metric(String name, String unit, String description) {
        double value = meterRegistry.find(name).meters().stream()
                .mapToDouble(meter -> meter.measure().iterator().hasNext()
                        ? meter.measure().iterator().next().getValue()
                        : 0)
                .sum();
        return new MetricSnapshot(name, Math.round(value * 100.0) / 100.0, unit, description);
    }

    private void incrementCounter(String name, String tagKey, String tagValue) {
        Counter.builder(name)
                .tag(tagKey, tagValue)
                .register(meterRegistry)
                .increment();
    }
}
