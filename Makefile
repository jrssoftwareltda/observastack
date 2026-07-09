.PHONY: test run docker-up docker-down simulate-traffic simulate-alert prometheus

test:
	mvn clean test

run:
	mvn spring-boot:run

docker-up:
	docker compose up --build

docker-down:
	docker compose down -v

simulate-traffic:
	curl -X POST "http://localhost:8080/api/v1/observability/traffic?requests=50&optimized=true" | jq

simulate-alert:
	curl -X POST "http://localhost:8080/api/v1/observability/alerts/simulate?type=latency" | jq

prometheus:
	curl "http://localhost:8080/actuator/prometheus" | head -40
