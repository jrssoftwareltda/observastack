# ObservaStack

**ObservaStack** is a JRS Software DevOps and observability case for modern APIs.

It demonstrates how a production-ready API can expose health checks, operational metrics, structured logs, simulated alerts, a simple dashboard and a before/after performance optimization comparison.

## Case Summary

**Title:** ObservaStack — API Monitoring and Logs  
**Tagline:** Observability for production-grade APIs.  
**Business value:** Make API behavior visible, measurable and easier to operate.  
**Main audience:** SaaS teams, software houses, startups and companies that need better production reliability.

## What This Project Shows

- `GET /health` custom health endpoint.
- Spring Boot Actuator health, info, metrics and Prometheus endpoints.
- Request metrics and latency measurement with Micrometer.
- Structured JSON logs for operational events.
- A simple static dashboard at `/`.
- Simulated alert generation for latency and error rate.
- Before/after optimization comparison.
- Prometheus and Grafana stack with Docker Compose.
- GitHub Actions CI with build, tests, Docker image build and Trivy scans.
- Dependabot automation.

## Architecture

```text
Client / Browser
      |
      v
ObservaStack API - Spring Boot
      |
      |-- /health
      |-- /api/v1/observability/report
      |-- /api/v1/observability/traffic
      |-- /api/v1/observability/alerts/simulate
      |-- /api/v1/observability/optimization/compare
      |-- /actuator/prometheus
      |
      +--> JSON structured logs
      +--> Micrometer metrics
      +--> Prometheus scrape
      +--> Grafana dashboard
```

## Tech Stack

- Java 21
- Spring Boot
- Spring Boot Actuator
- Micrometer
- Prometheus
- Grafana
- Docker
- Docker Compose
- GitHub Actions
- Trivy
- Dependabot

## Running Locally

### Option 1: Maven

```bash
mvn spring-boot:run
```

Open:

```text
http://localhost:8080
```

### Option 2: Docker Compose

```bash
docker compose up --build
```

Open:

```text
API dashboard: http://localhost:8080
Prometheus:    http://localhost:9090
Grafana:       http://localhost:3000
```

Grafana default credentials:

```text
username: admin
password: admin
```

## Main Endpoints

### Health

```bash
curl http://localhost:8080/health
```

### Observability Report

```bash
curl http://localhost:8080/api/v1/observability/report
```

### Simulate Traffic

```bash
curl -X POST "http://localhost:8080/api/v1/observability/traffic?requests=50&optimized=true"
```

### Simulate Latency Alert

```bash
curl -X POST "http://localhost:8080/api/v1/observability/alerts/simulate?type=latency"
```

### Simulate Error-Rate Alert

```bash
curl -X POST "http://localhost:8080/api/v1/observability/alerts/simulate?type=error-rate"
```

### Compare Before/After Optimization

```bash
curl http://localhost:8080/api/v1/observability/optimization/compare
```

### Prometheus Metrics

```bash
curl http://localhost:8080/actuator/prometheus
```

## Example Business Output

The optimization endpoint returns a comparison like this:

```json
{
  "scenario": "Search invoices by tenant in a high-volume API",
  "before": {
    "mode": "before",
    "durationMillis": 12,
    "resultCount": 400,
    "strategy": "Linear scan across all records"
  },
  "after": {
    "mode": "after",
    "durationMillis": 1,
    "resultCount": 400,
    "strategy": "Hash-map index by tenant"
  },
  "improvementPercent": 91.67,
  "businessMeaning": "The optimized path uses a precomputed index, reducing lookup cost and improving API latency predictability."
}
```

This makes the case easy to explain in client conversations: the system does not only run; it produces metrics that help teams understand performance, incidents and improvements.

## Structured Logs

The API emits JSON logs with operational fields such as:

```json
{
  "service": "observastack",
  "event": "api_request_observed",
  "endpoint": "traffic-simulator",
  "optimized": true,
  "success": true,
  "durationMs": 18
}
```

These fields make it easier to filter events in log tools such as Grafana Loki, ELK/OpenSearch, Datadog or cloud-native logging platforms.

## Prometheus Metrics Included

- `observastack_api_requests_total`
- `observastack_api_errors_total`
- `observastack_api_latency`
- `observastack_active_alerts`
- Default JVM, HTTP and Spring Boot Actuator metrics

## Suggested JRS Software Website Card

```text
ObservaStack
Observability for production-grade APIs.

Health checks, Prometheus metrics, structured logs, simulated alerts and API optimization comparison.

Stacks: Java, Spring Boot, Actuator, Micrometer, Prometheus, Grafana, Docker, GitHub Actions.
```

## Project Structure

```text
observastack/
  src/main/java/br/com/jrssoftware/observastack
  src/main/resources/static/index.html
  monitoring/prometheus
  monitoring/grafana
  docs
  .github/workflows
  Dockerfile
  docker-compose.yml
  README.md
```

## Tests

```bash
mvn clean test
```

## CI/CD

The GitHub Actions workflow performs:

1. Java 21 setup.
2. Maven build and tests.
3. Docker image build.
4. Trivy filesystem scan.
5. Trivy Docker image scan.

## License

MIT License.
