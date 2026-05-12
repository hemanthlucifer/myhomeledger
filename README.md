# MyHomeLedger (Spring Boot + Thymeleaf)

## Local

Run:

```bash
./gradlew bootRun
```

Open:

- `http://localhost:8080/myhomeledger/`

## Deploy to Render

This repo includes `render.yaml` for a Java web service.

### Required environment variables

Render should provide one of these:

- **`DATABASE_URL`**: `postgres://user:pass@host:port/db?...` (Render Postgres style)
- OR **`JDBC_DATABASE_URL`**: `jdbc:postgresql://host:port/db?...`

Optional overrides:

- **`DB_USERNAME`**, **`DB_PASSWORD`** (only needed if your URL doesn't include user/pass)
- **`JWT_SECRET`** (required in production; Render generates one if you use `render.yaml`)

### Notes

- Render injects **`PORT`**; the app binds to it via `server.port=${PORT:8080}`.
- Context path is **`/myhomeledger`**, so the landing page is `/myhomeledger/`.

