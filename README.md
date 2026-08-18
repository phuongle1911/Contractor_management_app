# Contractor Management App

## Main features
- user log in, log out
- admin user can assign/ register user account
- User choose which project they are working on, location, budget,  any requirements for specific projects
- based on those information, AI recommend suitable contractor using information in the app and information in google (google review,...)
- sending a quote request to potential contractors 
- when user decides to select a contractor, app send them a link for them to fill out required information and documents
- user have ability to set up template of information request form
- use those information to save info of the registered contractor, with main services they provide. 
- user give feedback for contractors in the app when they finished working with contractor


A monorepo containing a Next.js frontend, Spring Boot backend, and PostgreSQL database.

## Project structure

```text
.
├── frontend/       # Next.js application
├── backend/        # Spring Boot API
└── compose.yaml    # Local PostgreSQL database
```

## Prerequisites

- Node.js 20.9 or newer
- npm
- Java 21 or newer
- Docker with Docker Compose

Maven does not need to be installed globally because the backend includes the Maven Wrapper.

## Local setup

### 1. Configure the frontend

```bash
cp frontend/.env.example frontend/.env.local
```

### 2. Start PostgreSQL

```bash
docker compose up -d
```

Check that it is healthy:

```bash
docker compose ps
```

### 3. Start the backend

In a separate terminal:

```bash
cd backend
./mvnw spring-boot:run
```

The API runs at `http://localhost:8080`.

Health check:

```bash
curl http://localhost:8080/api/health
```

### 4. Start the frontend

In another terminal:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:3000`.

## Testing changes before committing

After implementing a feature or fixing a bug, complete the following checks before committing your changes.

First, add or update automated tests for the behavior that changed. Backend tests belong under `backend/src/test/`. A frontend test runner has not been configured yet, so frontend changes currently rely on linting, production builds, and the manual checks below.

### 1. Run the backend tests

From the repository root:

```bash
cd backend
./mvnw test
cd ..
```

The command should finish with `BUILD SUCCESS`.

### 2. Check the frontend

```bash
cd frontend
npm run lint
npm run build
cd ..
```

Both commands must complete without errors.

### 3. Test the complete application locally

Start PostgreSQL:

```bash
docker compose up -d
```

Start the backend in a separate terminal:

```bash
cd backend
./mvnw spring-boot:run
```

Start the frontend in another terminal:

```bash
cd frontend
npm run dev
```

Then verify:

- `http://localhost:3000` loads successfully.
- `http://localhost:8080/api/health` returns `{"status":"ok"}`.
- The feature or bug fix works through the user interface.
- Relevant error and empty states behave correctly.
- The browser console and backend terminal contain no unexpected errors.

### 4. Check database changes

If the change modifies the database schema:

- Add a new versioned Flyway migration under `backend/src/main/resources/db/migration/`.
- Do not edit a migration that has already been applied or shared.
- Restart the backend and confirm Flyway applies the new migration successfully.
- Run the backend tests again after applying the migration.

### 5. Review the changes

From the repository root:

```bash
git status --short
git diff
```

Confirm that no generated files, local environment files, credentials, or unrelated changes will be committed.

## Environment variables

### Frontend

| Variable | Local value |
| --- | --- |
| `NEXT_PUBLIC_API_URL` | `http://localhost:8080` |

### Backend

| Variable | Local default |
| --- | --- |
| `DB_URL` | `jdbc:postgresql://localhost:5432/contractor_management` |
| `DB_USERNAME` | `contractor_app` |
| `DB_PASSWORD` | `contractor_dev_password` |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:3000` |

Local defaults are for development only. Production secrets must be supplied through the deployment environment.

## Stopping the local database

```bash
docker compose down
```
