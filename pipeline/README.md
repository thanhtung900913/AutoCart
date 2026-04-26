# Pipeline Module

## Overview
The `pipeline` module orchestrates data movement, stream processing, and recommendation engine services. It is responsible for change data capture (CDC), real-time processing, and generating product embeddings and recommendations.

## Components
- **Flink**: Apache Flink cluster (JobManager & TaskManager) for stateful stream processing.
- **Kafka & Debezium**: Apache Kafka for message brokering and Debezium for database Change Data Capture (CDC).
- **RecSys**: Recommendation system consisting of embedding generation and search engine services.

## Directory Structure
```text
pipeline/
├── flink/           # Flink cluster compose file and job configurations
├── kafka-dbz/       # Kafka, Zookeeper, Debezium, and Kafka UI compose files
└── recsys/          # Recommendation system APIs, embeddings, and compose files
```

## How to Run
Navigate to each sub-directory and start the services using `docker compose up`.
```bash
cd pipeline/kafka-dbz
docker compose up -d
```
Repeat for `flink` and `recsys`. Ensure that the `cdc_global_network` is available, as these services rely on it for inter-container communication.

## Services, Ports, and URLs

| Service | Container Name | Local Port | Container Port | URL / Address |
|---------|----------------|------------|----------------|---------------|
| Flink JobManager | `flink_jobmanager` | 8081 | 8081 | http://localhost:8081 |
| Flink TaskManager| `flink_taskmanager`| - | - | N/A |
| Kafka | `kafka` | 9092 | 9092 | localhost:9092 |
| Zookeeper | `zookeeper` | - | - | N/A |
| Debezium Connect | `debezium_connect` | 8083 | 8083 | http://localhost:8083 |
| Kafka UI | `kafka_ui` | 8080 | 8080 | http://localhost:8080 |
| Embedding API | `embedding` | 8065 | 8000 | http://localhost:8065 |
| Search Engine | `search-engine` | - | - | N/A |
| RecSys | `recsys` | - | - | N/A |
