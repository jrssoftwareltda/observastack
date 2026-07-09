package br.com.jrssoftware.observastack.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MetricsConfig {

    private final AtomicInteger activeAlerts = new AtomicInteger(0);

    public MetricsConfig(MeterRegistry meterRegistry) {
        meterRegistry.gauge("observastack_active_alerts", activeAlerts);
    }

    public AtomicInteger activeAlerts() {
        return activeAlerts;
    }
}
