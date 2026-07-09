# API Examples

## Health

```bash
curl http://localhost:8080/health
```

## Report

```bash
curl http://localhost:8080/api/v1/observability/report
```

## Generate Optimized Traffic

```bash
curl -X POST "http://localhost:8080/api/v1/observability/traffic?requests=100&optimized=true"
```

## Generate Slow Traffic

```bash
curl -X POST "http://localhost:8080/api/v1/observability/traffic?requests=100&optimized=false"
```

## Trigger Latency Alert

```bash
curl -X POST "http://localhost:8080/api/v1/observability/alerts/simulate?type=latency"
```

## Trigger Error-Rate Alert

```bash
curl -X POST "http://localhost:8080/api/v1/observability/alerts/simulate?type=error-rate"
```

## Compare Optimization

```bash
curl http://localhost:8080/api/v1/observability/optimization/compare
```

## Prometheus

```bash
curl http://localhost:8080/actuator/prometheus
```
