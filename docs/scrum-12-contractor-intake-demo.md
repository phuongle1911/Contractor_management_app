# SCRUM-12 contractor intake demo

## Automated tests with a fresh database

The automated tests use the same sample identifiers as the manual demo. Reset
the local database first if the demo contractors have already been created.

> **Warning:** `docker compose down -v` permanently deletes all data in this
> project's local PostgreSQL volume.

Run these commands from the repository root:

```bash
docker compose down -v
docker compose up -d postgres

cd backend

POSTGRES_URL=jdbc:postgresql://localhost:5433/contractor_management \
POSTGRES_USER=contractor_app \
POSTGRES_PASSWORD=contractor_dev_password \
./mvnw test
```

A successful run ends with output similar to:

```text
Tests run: 2, Failures: 0, Errors: 0
BUILD SUCCESS
```

## Manual API demonstration

Build and start the PostgreSQL and backend containers before running these commands:

```bash
docker compose up -d --build
```

Docker Compose starts one container for PostgreSQL and one for the Spring Boot
backend. Rebuilding ensures the backend container includes the latest local code.

The following requests create three contractors in PostgreSQL:

```bash
curl -i -X POST http://localhost:8080/api/contractors \
  -H 'Content-Type: application/json' \
  -d '{
    "contractorName": "Bright Spark Electrical",
    "location": "Melbourne VIC",
    "email": "bright-spark@example.com",
    "abn": "11111111111"
  }'

curl -i -X POST http://localhost:8080/api/contractors \
  -H 'Content-Type: application/json' \
  -d '{
    "contractorName": "Reliable Plumbing Co",
    "location": "Geelong VIC",
    "email": "reliable-plumbing@example.com",
    "abn": "22222222222"
  }'

curl -i -X POST http://localhost:8080/api/contractors \
  -H 'Content-Type: application/json' \
  -d '{
    "contractorName": "Southern Build Group",
    "location": "Ballarat VIC",
    "email": "southern-build@example.com",
    "abn": "33333333333"
  }'
```

Copy the first contractor's `id` from its response and use it in place of `<id>` below.

Assign services:

```bash
curl -i -X PUT http://localhost:8080/api/contractors/<id>/services \
  -H 'Content-Type: application/json' \
  -d '["electrical", "solar", "commercial fit-out"]'
```

Store document metadata (the file itself is not uploaded):

```bash
curl -i -X POST http://localhost:8080/api/contractors/<id>/documents \
  -H 'Content-Type: application/json' \
  -d '{
    "fileName": "electrical-licence.pdf",
    "versionNumber": 1
  }'
```

Submit checklist details as JSON:

```bash
curl -i -X PUT http://localhost:8080/api/contractors/<id>/checklist \
  -H 'Content-Type: application/json' \
  -d '{
    "insuranceVerified": true,
    "safetyInductionComplete": true,
    "approvedWorkTypes": ["commercial", "residential"]
  }'
```

Verify the stored contractors directly in PostgreSQL:

```bash
docker compose exec postgres psql \
  -U contractor_app \
  -d contractor_management \
  -c "SELECT id, contractor_name, checklist FROM contractors;"

docker compose exec postgres psql \
  -U contractor_app \
  -d contractor_management \
  -c "SELECT * FROM contractor_services;"

docker compose exec postgres psql \
  -U contractor_app \
  -d contractor_management \
  -c "SELECT * FROM contractor_documents;"
```
