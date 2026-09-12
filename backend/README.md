# LeaveWeb backend

Spring Boot 3.4 / Java 21 REST API for the LeaveWeb React client. The backend uses MongoDB for persistence, BCrypt for password hashing, and stateless JWT authentication. It does not connect to or modify the React project.

## Requirements

- Java 21
- Maven 3.9+
- MongoDB running locally at `mongodb://localhost:27017`

The default database is `leaveweb`. Copy `.env.example` to `.env` or export the values in your shell. Never use the sample JWT secret outside local development.

## Configuration

| Variable | Default | Purpose |
| --- | --- | --- |
| `MONGODB_URI` | `mongodb://localhost:27017/leaveweb` | Mongo connection URI |
| `JWT_SECRET` | development placeholder | HMAC signing secret, at least 32 bytes |
| `JWT_EXPIRATION_MS` | `86400000` | Token lifetime |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:5173` | Comma-separated frontend origins |
| `SPRING_PROFILES_ACTIVE` | `dev` | Enables development seed data |

The `dev` profile creates an admin (`admin@leaveweb.com` / `Admin@123`), a sample employee (`employee@leaveweb.com` / `Employee@123`), and default leave types only when they do not already exist. Change these credentials immediately in any shared environment.

## Run

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

The API listens on `http://localhost:8080`. MongoDB must be running before startup. Tests can be run with `mvn test`.

## API endpoints

All endpoints below are prefixed with `/api`. Auth endpoints are public; all others require `Authorization: Bearer <jwt>` unless noted.

| Method | Endpoint | Access |
| --- | --- | --- |
| POST | `/auth/register` | Public |
| POST | `/auth/login` | Public |
| GET | `/auth/me` | Authenticated |
| GET | `/users/me` | Authenticated |
| GET/PUT/DELETE | `/users/{id}` | Admin |
| GET | `/dashboard` | Authenticated |
| POST/GET/PUT/DELETE | `/leaves`, `/leaves/{id}` | Authenticated, own records |
| POST | `/leaves/{id}/cancel` | Owner, pending only |
| GET | `/leave-types`, `/leave-types/{id}` | Authenticated |
| POST/PUT/DELETE | `/leave-types`, `/leave-types/{id}` | Admin |
| GET/PUT/DELETE | `/notifications`, `/notifications/{id}/read` | Authenticated, own records |
| GET | `/admin/dashboard` | Admin |
| GET | `/admin/users` | Admin |
| GET | `/admin/leaves` | Admin |
| POST | `/admin/leaves/{id}/approve` | Admin |
| POST | `/admin/leaves/{id}/reject` | Admin, JSON `{ "reason": "..." }` |

Successful login/register returns `{ "token": "...", "user": { ... } }`. Passwords are never included in user responses. Validation and business failures use `{ "timestamp", "status", "message", "path" }`.

## Authentication flow

1. Register or log in with email and password.
2. Store the returned JWT in the frontend session abstraction.
3. Send it in the `Authorization` header for protected calls.
4. The JWT filter loads the user and assigns `ROLE_EMPLOYEE` or `ROLE_ADMIN`.
5. Expired/invalid tokens are rejected by Spring Security.

## Project structure

`model` contains Mongo documents and enums. `repository` contains Spring Data interfaces. `service` owns validation, balance calculations, approvals, notifications, and dashboard aggregation. `controller` exposes REST DTOs. `security` owns JWT verification. `config` owns security, CORS, and development seed data.

## Tests

`LeaveServiceTest` covers inclusive duration, invalid date ranges, and balance enforcement. `AuthServiceTest` covers the login token/user contract. Run the complete suite with `mvn test`; add Mongo/Testcontainers integration coverage when CI provides a MongoDB service.