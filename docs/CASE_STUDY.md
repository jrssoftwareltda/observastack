# ObservaStack — Case Study

## Context

Many APIs are deployed without enough visibility. They may expose business features, but teams often struggle to answer simple operational questions:

- Is the API healthy?
- Which endpoints are slow?
- Are errors increasing?
- Did an optimization actually improve latency?
- Can logs be filtered by meaningful fields?

ObservaStack is a compact case showing how JRS Software approaches API observability with practical engineering.

## Problem

A growing API needs more than code. It needs production signals:

- Health checks for uptime and readiness.
- Metrics for performance and reliability.
- Logs that help debugging.
- Alerts that show when behavior is abnormal.
- A clear way to compare technical improvements.

## Solution

ObservaStack implements a Spring Boot API with:

- Custom `/health` endpoint.
- Spring Boot Actuator.
- Prometheus metrics.
- Grafana dashboard provisioning.
- JSON structured logs.
- Traffic simulator.
- Alert simulator.
- Before/after optimization comparison.

## Demonstration Flow

1. Open `http://localhost:8080`.
2. Click `Simulate slow traffic`.
3. Click `Simulate optimized traffic`.
4. Trigger an alert.
5. Open Prometheus and Grafana.
6. Show the before/after optimization output.

## Why It Works Well as a Portfolio Case

The case is visual, easy to run and easy to explain. It connects DevOps, SRE, software architecture and performance engineering in a small project.

## Website Copy

**ObservaStack**  
Observability for production-grade APIs.

Health checks, Prometheus metrics, structured logs, simulated alerts and API optimization comparison.

**Stacks:** Java, Spring Boot, Actuator, Micrometer, Prometheus, Grafana, Docker, GitHub Actions.
