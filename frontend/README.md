# LeaveWeb frontend

LeaveWeb is a Vite + React leave-management interface prepared for a Spring Boot REST API. The current screens use isolated sample data for UI development; no passwords, database connections, or backend writes are implemented.

## Install and run

```bash
npm install
copy .env.example .env
npm run dev
```

For macOS/Linux, use `cp .env.example .env`. Production output is checked with `npm run build` and can be previewed with `npm run preview`.

## Structure

`src/components` contains reusable controls and cards. `src/layouts` owns route protection and the authenticated shell. `src/pages` contains employee and admin views. `src/context` owns browser-session auth and notifications. `src/data` is the only mock-data location. `src/services` contains Axios-backed API modules ready to replace the mock flows.

## Environment

Set `VITE_API_BASE_URL` in `.env` to the Spring Boot API root. The example value is `http://localhost:8080/api`.

## Routes

Public: `/login`, `/register`.

Employee: `/dashboard`, `/apply-leave`, `/my-leaves`, `/leaves/:id`, `/profile`, `/notifications`.

Admin: `/admin`, `/admin/users`, `/admin/leaves`, `/admin/leave-types`, `/admin/reports`.

Use any email and password in the demo login. `employee@leaveweb.com` previews a normal employee; any other email previews the admin navigation.

## Expected Spring Boot API

The service layer currently expects:

- `POST /api/auth/login`, `POST /api/auth/register`, `GET /api/auth/me`, `POST /api/auth/logout`
- `GET /api/leaves`, `GET /api/leaves/{id}`, `POST /api/leaves`, `PATCH /api/leaves/{id}/cancel`
- `PATCH /api/leaves/{id}/approve`, `PATCH /api/leaves/{id}/reject`
- `GET /api/users`, `POST /api/users`, `PUT /api/users/{id}`, `DELETE /api/users/{id}`
- `GET /api/notifications`, `PATCH /api/notifications/{id}/read`, `DELETE /api/notifications/{id}`
- `GET /api/admin/leave-types`, `POST /api/admin/leave-types`, `PUT /api/admin/leave-types/{id}`, `DELETE /api/admin/leave-types/{id}`
- `GET /api/admin/reports`

Axios reads the JWT from the `leaveweb_token` browser key and sends it as a Bearer token. When the backend is available, replace the demo calls in the contexts/pages with the corresponding service functions without changing component URLs.