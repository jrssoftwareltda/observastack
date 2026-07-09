# Architecture

ObservaStack follows a simple layered structure:

```text
controller -> service -> dto
```

## Controllers

- `HealthController`: exposes `/health`.
- `ObservabilityController`: exposes report, traffic, alert, logs and optimization endpoints.

## Services

- `ObservabilityService`: records metrics, writes structured logs and simulates alerts.
- `InventorySearchService`: simulates a performance improvement from linear scan to indexed lookup.

## Observability Components

- Spring Boot Actuator exposes operational endpoints.
- Micrometer records custom metrics.
- Prometheus scrapes `/actuator/prometheus`.
- Grafana loads a provisioned dashboard.
- Logback emits JSON logs.

## Deployment Model

The Docker Compose stack runs:

- ObservaStack API
- Prometheus
- Grafana

This keeps the case self-contained and easy to demonstrate.
