# Infrastructure Module (infras)

## Overview
The `infras` module contains the core databases and storage services required for the AutoCart application. It provides relational and NoSQL databases, columnar analytical databases, and object storage to support transactional data, analytics, and file storage.

## Components
- **ClickHouse**: Columnar database for fast analytical queries.
- **MinIO**: S3-compatible object storage.
- **MongoDB**: NoSQL database for flexible document storage.
- **Postgres**: Relational database for structured transactional data.

## Directory Structure
```text
infras/
├── click-house/     # ClickHouse configuration, schemas, and compose file
├── metabase/        # Metabase configurations
├── minio/           # MinIO compose file
├── mongo/           # MongoDB configuration and compose file
└── postgres/        # PostgreSQL schema, config, and compose file
```

## How to Run
To spin up all infrastructure services, you can run the `docker compose up` command in each of the component directories. For example:
```bash
cd infras/postgres
docker compose up -d
```
Repeat this for `mongo`, `click-house`, and `minio` as needed. Make sure the external Docker network (e.g., `cdc_global_network`) is created before starting the services that depend on it.

## Services, Ports, and URLs

| Service | Container Name | Local Port | Container Port | URL |
|---------|----------------|------------|----------------|-----|
| ClickHouse | `clickhouse` | 8123 (HTTP), 9000 (TCP) | 8123, 9000 | http://localhost:8123 |
| MinIO (API) | `minio` | 9000 | 9000 | http://localhost:9000 |
| MinIO (Console)| `minio` | 9001 | 9001 | http://localhost:9001 |
| MongoDB | `autocart_mongo_db` | 27017 | 27017 | mongodb://localhost:27017 |
| PostgreSQL | `autocart_postgres` | 5432 | 5432 | postgresql://localhost:5432 |
