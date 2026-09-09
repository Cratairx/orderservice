# Hotel Booking Platform

A small hotel booking system built as four separate repositories that together form one application: customers can be registered, rooms can be booked, and completed stays can be reviewed.

| Repo | Role | Port | Database |
|---|---|---|---|
| [`customerservice`](https://github.com/Cratairx/customerservice) | Manages customer records | `8080` | `customerdb` (MySQL) |
| [`orderservice`](https://github.com/Cratairx/orderservice) | Manages rooms & bookings | `8081` | `bookingdb` (MySQL) |
| [`reviewservice`](https://github.com/EmilDrougge/reviewservice) | Manages reviews of completed stays | `8082` | `review_db` (MySQL) |
| [`FrontEnd`](https://github.com/Cratairx/FrontEnd) | Client UI | — | — |

All three backend services are Spring Boot (Java 17) apps, each with its own MySQL database, and they talk to each other over plain HTTP using each other's Docker service name.

> **Note on the frontend:** the `FrontEnd` repo, as it currently stands, is a static HTML/CSS practice site (tutorial pages, a bootstrap exercise, lorem-ipsum content) with no JavaScript and no calls to any of the three APIs. It isn't wired up to the backend yet. The backend services already have CORS opened for `http://localhost:5173` (the default Vite dev server port), which tells us the intended frontend is a JS app (React/Vue/etc. served by Vite) that hasn't landed in that repo yet. Section 4 below covers both: how to serve the existing static files, and how to interact with the running system directly (which is the only way to actually exercise the app today).

## 1. How the services fit together

```
                         ┌─────────────────────┐
                         │   Browser / Client   │
                         └──────────┬───────────┘
                                    │ HTTP
              ┌─────────────────────┼─────────────────────┐
              │                     │                      │
              ▼                     ▼                      ▼
     ┌────────────────┐   ┌────────────────────┐   ┌──────────────────┐
     │ customer-service│   │  booking-service    │   │  review-service   │
     │   :8080         │◄──┤  (orderservice)     │◄──┤  :8082            │
     │                 │   │   :8081             │   │                   │
     └────────┬────────┘   └──────────┬──────────┘   └─────────┬─────────┘
              │                       │                         │
              ▼                       ▼                         ▼
        customer-db              booking-db                review-db
        (MySQL, customerdb)    (MySQL, bookingdb)        (MySQL, review_db)
```

Cross-service calls (all plain REST over the Docker network):

- **booking-service → customer-service**: when a booking is created, it calls `GET /api/customer/{id}` to confirm the customer exists before saving the booking.
- **customer-service → booking-service**: when a customer is deleted, it calls `GET /api/bookings/exists?customerId={id}` first — a customer with active bookings cannot be deleted (returns `409 Conflict`).
- **review-service → booking-service**: when a review is created, it calls `GET /api/bookings/getbooking/{id}` to fetch the booking, confirms the review's `customerId` matches the booking's owner, confirms the stay's `endDate` has already passed, and confirms a review doesn't already exist for that booking — otherwise it rejects with `403 Forbidden`.

Because of this, **booking-service needs customer-service reachable, and review-service needs booking-service reachable** — start them in that order (the Docker Compose file below handles this automatically).

## 2. Prerequisites

- Docker and Docker Compose
- The four repositories cloned as sibling folders:

```
hotel-app/
├── customerservice/
├── orderservice/
├── reviewservice/
├── FrontEnd/
└── docker-compose.yml   ← the file from this README
```

```bash
mkdir hotel-app && cd hotel-app
git clone https://github.com/Cratairx/customerservice.git
git clone https://github.com/Cratairx/orderservice.git
git clone https://github.com/EmilDrougge/reviewservice.git
git clone https://github.com/Cratairx/FrontEnd.git
```

Each backend repo already ships its own `Dockerfile` (multi-stage Maven build → runnable jar), so no local Java/Maven install is required — Docker handles the build.

## 3. Running the whole stack in Docker

Save the following as `docker-compose.yml` at the root of `hotel-app/` (next to the three cloned service folders). It builds all three services and their databases, and names each container to match the hostnames the code already expects (`customer-db`, `customer-service`, `booking-db`, `booking-service`, `review-db`, `review-service`), so no extra environment variables are needed for the services to find each other.

```yaml
services:

  # ---------- Customer Service ----------
  customer-db:
    image: mysql:8
    container_name: customer-db
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: customerdb
    ports:
      - "3306:3306"
    volumes:
      - customer-db-data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
      interval: 5s
      timeout: 5s
      retries: 20

  customer-service:
    build: ./customerservice
    container_name: customer-service
    ports:
      - "8080:8080"
    depends_on:
      customer-db:
        condition: service_healthy

  # ---------- Booking / Order Service ----------
  booking-db:
    image: mysql:8
    container_name: booking-db
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: bookingdb
    ports:
      - "3307:3306"
    volumes:
      - booking-db-data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
      interval: 5s
      timeout: 5s
      retries: 20

  booking-service:
    build: ./orderservice
    container_name: booking-service
    ports:
      - "8081:8081"
    depends_on:
      booking-db:
        condition: service_healthy
      customer-service:
        condition: service_started

  # ---------- Review Service ----------
  review-db:
    image: mysql:8
    container_name: review-db
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: review_db
    ports:
      - "3308:3306"
    volumes:
      - review-db-data:/var/lib/mysql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
      interval: 5s
      timeout: 5s
      retries: 20

  review-service:
    build: ./reviewservice
    container_name: review-service
    ports:
      - "8082:8082"
    depends_on:
      review-db:
        condition: service_healthy
      booking-service:
        condition: service_started

volumes:
  customer-db-data:
  booking-db-data:
  review-db-data:
```

Then, from `hotel-app/`:

```bash
docker compose up --build
```

This will:
1. Build the three Spring Boot jars inside Docker (Maven, Java 17).
2. Start the three MySQL databases and wait for them to report healthy.
3. Start `customer-service`, then `booking-service`, then `review-service`.

On first boot:
- `customer-service` seeds three sample customers (John Doe, Jane Smith, Bob Johnson).
- `booking-service` seeds three sample rooms (`101`/SINGLE, `102`/SINGLE, `103`/DOUBLE).

Once everything is up:

| Service | URL |
|---|---|
| Customer Service | http://localhost:8080/api |
| Booking Service | http://localhost:8081/api |
| Review Service | http://localhost:8082/api |

To stop everything: `docker compose down` (add `-v` to also wipe the database volumes).

## 4. Interacting with the app

### 4a. Via the FrontEnd repo (static pages)

The `FrontEnd` repo can be opened as-is, but as noted above it's a set of unrelated HTML/CSS learning exercises (`Index.html`, `CssTutorial.html`, a Bootstrap demo, etc.) — it does not call any of the three APIs, so opening it won't let you manage customers, bookings, or reviews. If you just want to view the pages:

```bash
cd FrontEnd
npx serve .        # or: python3 -m http.server 5500
```

then browse to whatever port it prints.

To actually build a working frontend for this app, point it at the three URLs in the table above and run it on **port 5173** (`npm create vite@latest` and `npm run dev` defaults to this) — the backend's CORS policy is already configured to allow requests from `http://localhost:5173` specifically, so a Vite dev server needs no backend changes to talk to the APIs.

### 4b. Directly against the APIs (works today)

Until a real frontend exists, `curl`, Postman, or Insomnia are the way to exercise the system. All endpoints below assume the stack from Section 3 is running.

**Customer Service — `http://localhost:8080/api`**

| Method | Path | Body | Notes |
|---|---|---|---|
| GET | `/customers` | — | List all customers |
| GET | `/customer/{id}` | — | Get one customer |
| POST | `/customer` | `{"firstName","lastName","email"}` | Register a customer |
| POST | `/customer/{id}` | `{"firstName","lastName","email"}` | Update a customer |
| POST | `/customer/{id}/delete` | — | Delete a customer (fails with `409` if they have active bookings) |

```bash
curl -X POST http://localhost:8080/api/customer \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Ada","lastName":"Lovelace","email":"ada@example.com"}'
```

**Booking Service — `http://localhost:8081/api`**

| Method | Path | Params | Notes |
|---|---|---|---|
| GET | `/rooms` | — | List all rooms |
| GET | `/rooms/{id}` | — | Get one room |
| POST | `/rooms` | `roomNumber`, `roomType` (`SINGLE`\|`DOUBLE`) | Create a room |
| PUT | `/rooms/{id}` | `roomNumber`, `roomType` | Edit a room |
| DELETE | `/rooms/{id}` | — | Delete a room |
| GET | `/bookings` | — | List all bookings |
| GET | `/bookings/available` | `startDate`, `endDate` (`YYYY-MM-DD`) | Rooms free for a date range |
| POST | `/bookings` | `customerId`, `roomId`, `startDate`, `endDate` | Create a booking |
| PUT | `/bookings/{id}` | `roomId`, `startDate`, `endDate` | Update a booking |
| DELETE | `/bookings/{id}` | — | Delete a booking |
| GET | `/bookings/getbooking/{id}` | — | Get a booking (looked up by booking id) |
| GET | `/bookings/exists` | `customerId` | Whether a customer has any bookings |

Booking creation/update/room creation take query params, not a JSON body:

```bash
curl -X POST "http://localhost:8081/api/bookings?customerId=1&roomId=1&startDate=2025-01-01&endDate=2025-01-05"
```

**Review Service — `http://localhost:8082/api`**

| Method | Path | Body / Params | Notes |
|---|---|---|---|
| POST | `/reviews` | `{"bookingId","customerId","rating","comment"}` | Review a completed stay |
| GET | `/reviews/room/{roomId}` | — | All reviews for a room |

A review is only accepted if: the booking exists, `customerId` matches the customer on that booking, the stay's `endDate` is in the past, and no review already exists for that booking.

```bash
curl -X POST http://localhost:8082/api/reviews \
  -H "Content-Type: application/json" \
  -d '{"bookingId":1,"customerId":1,"rating":5,"comment":"Great stay!"}'
```

### Typical end-to-end flow

1. `POST /api/customer` (customer-service) → get a `customerId`.
2. `GET /api/bookings/available?startDate=...&endDate=...` (booking-service) → pick a free room.
3. `POST /api/bookings` (booking-service) with that `customerId` and `roomId`.
4. After the `endDate` has passed, `GET /api/bookings/getbooking/{id}` to find the booking id, then `POST /api/reviews` (review-service) referencing it.

## 5. Notes / known quirks

- Each repo also ships its own standalone `docker-compose.yml` / `k8s/` manifests for running that single service in isolation during development — these are useful for local iteration on one service, but the compose file in Section 3 is what's needed to run the full application together.
- `customerservice`'s Kubernetes/legacy compose files reference a `booking-service.base-url` property, but that value is currently unused in code — the actual cross-call to booking-service is hardcoded to `http://booking-service:8081`, which is why the container names in this compose file matter.
