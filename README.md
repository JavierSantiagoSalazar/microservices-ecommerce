# Inventory Management System - Backend

Backend for an inventory management system based on microservices using Spring Boot 3.5, Java 17 and MySQL.

## 📋 Table of Contents

- [Architecture](#architecture)
- [Technologies](#technologies)
- [Prerequisites](#prerequisites)
- [Installation and Run](#installation-and-run)
- [Project Structure](#project-structure)
- [API Endpoints](#api-endpoints)
- [Technical Decisions](#technical-decisions)
- [Testing](#testing)
- [Diagrams](#diagrams)
- [Security](#security)
- [Future Improvements](#future-improvements)
- [Author](#author)

## Architecture

The system is composed of two independent microservices:

### Product Service (Port 8081)
- Full CRUD for products
- Independent MySQL database
- Hexagonal Architecture (Ports & Adapters)

### Inventory Service (Port 8082)
- Inventory management linked to products
- Hexagonal Architecture (Ports & Adapters)
- Communication with Product Service via WebClient
- Resilience using Resilience4j (Circuit Breaker, Retry, Timeout)
- Independent MySQL database

## Technologies

This is a **Java monorepo with a multi-module structure**: Both microservices (Product and Inventory), located in their own folders (`/product` and `/inventory`), are independent applications but share a common Gradle build configuration, dependencies, and tools.

### Backend

- **Java 17 (LTS)**  
  Main language for developing the microservices.
- **Spring Boot 3.5.8**  
  Enterprise framework for building production-ready microservices.
  - **Spring Web:** For building REST APIs.
  - **Spring Data JPA:** Simplifies database access.
  - **Spring Security:** Used to implement API Key authentication.
  - **Spring WebFlux:** Uses WebClient for inter-service HTTP communication.
- **MySQL 8.4**  
  Relational database for persistence. Each service has its own dedicated instance/container.
- **Resilience4j**  
  Fault-tolerance library for Circuit Breaker, Retry, and Timeout logic in service-to-service calls.
- **Katharsis (JSON:API implementation)**  
  Provides automatic compliance with the JSON:API standard for API responses.
- **Lombok**  
  Reduces boilerplate in code (getters, setters, constructors, etc.).
- **MapStruct**  
  Mapping between domain models and DTOs.
- **SpringDoc OpenAPI / Swagger UI**  
  Automatically generates documentation and interactive API explorer for endpoints.

### Build, DevOps, Containerization

- **Gradle** (Kotlin DSL)  
  Build tool and dependency manager for the entire monorepo. Centralized configuration in the root.
- **Docker & Docker Compose**  
  Containerization for local development. Each service and its database run in isolated containers. `docker-compose.yml` defines the topology.

### Testing

- **JUnit 5**  
  For writing unit and integration tests.
- **Mockito**  
  Mocking framework for isolation in tests.
- **Spring Security Test**  
  For authentication-related tests.
- **H2 Database**  
  In-memory database for fast and clean test suites.

---

### 🏗️ Monorepo Multi-Module Structure

- Both microservices (`product`, `inventory`) reside in a **single repository**, following a **multi-module Gradle** setup (`settings.gradle.kts`).
- **Shared build configuration and dependencies:** Common versions and libraries are declared in the root Gradle scripts. This ensures uniformity and reduces maintenance overhead.
- **Independent deployment:** Each service can be built, tested, and run independently, yet share the benefits of centralized configuration and dependency management.

## Prerequisites

Before running the project, ensure you have:

1. **Docker Desktop** (version 20.10 or higher)
   - Windows / macOS / Linux with Docker Engine and Docker Compose v2

2. **Git** (to clone the repository)

3. **JDK 17** (only required if you want to build/run locally without Docker)

---

## Installation and Run

### Option 1: Docker (Recommended)

This is the easiest way to run the whole system. Docker will:

- Start both MySQL databases (for product and inventory microservices)
- Build both microservices (Product and Inventory)
- Create the required network for service-to-service communication
- Run the complete stack with a single command

#### Steps

1. **Clone the repository**

```bash
git clone https:/github.com/JavierSantiagoSalazar/microservices-ecommerce.git
cd microservices-ecommerce
```

2. **Ensure Docker Desktop is running**

- Start Docker Desktop
- Wait until it shows "Docker Desktop is running"

3. **Check Docker installation**

```bash
docker --version
docker compose version
```

Make sure both commands output their version information.

4. **Build and start all services**

From the project root (where `docker-compose.yml` is located):

```bash
docker compose up --build
```

This will:

- Pull MySQL images
- Build `product` and `inventory` JARs via Gradle
- Build Docker images for both microservices
- Start:
  - `product-db` (MySQL for product service)
  - `inventory-db` (MySQL for inventory service)
  - `product-service`
  - `inventory-service`

5. **Verify containers**

In another terminal:

```bash
docker compose ps
```

You should see something like:

```
NAME               STATUS        PORTS
product-db         Up           0.0.0.0:3307->3306/tcp
inventory-db       Up           0.0.0.0:3308->3306/tcp
product-service    Up           0.0.0.0:8081->8081/tcp
inventory-service  Up           0.0.0.0:8082->8082/tcp
```

![docker](https://github.com/user-attachments/assets/66bc8967-7170-4201-876f-574a8171bcb3)


6. **Access services**

- Product Swagger UI: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- Inventory Swagger UI: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

---

#### API Key Authentication

All endpoints (except Swagger UI and health checks) are protected with an API Key.

Header to send:

```
X-API-Key: mi-clave-secreta-2025
```

In Swagger UI:

1. Click the **Authorize** button (lock icon)
2. Use `X-API-Key` as the header name
3. Enter `mi-clave-secreta-2025` as value
4. Click **Authorize**

---

To stop the stack:

```bash
docker compose down
```

To also remove volumes and data:

```bash
docker compose down -v
```

### Option 2: Local Run (Without Docker)

Use this option if you want to debug or run the microservices directly in your IDE (IntelliJ, Eclipse, etc.).

#### Additional requirements

- Local MySQL 8.x server
- JDK 17
- Gradle (the project includes the Gradle Wrapper, so no global install required)

#### Steps

1. **Create databases**

In your local MySQL server, create both databases:

```sql
CREATE DATABASE product_db;
CREATE DATABASE inventory_db;
```

2. **Configure credentials**

Default local Spring Boot configuration (`application.properties`) expects:

- Username: `root`
- Password: `password`
- Host: `localhost`
- Port: `3306`

If your local MySQL setup uses different credentials or port, update the following files accordingly:

- `product/src/main/resources/application.properties`
- `inventory/src/main/resources/application.properties`

3. **Build both microservices**

From the project root directory:

```bash
./gradlew :product:clean :product:build -x test
./gradlew :inventory:clean :inventory:build -x test
```
*(Use `gradlew.bat` on Windows if needed.)*

4. **Run Product Service**

```bash
cd product
java -jar build/libs/product-0.0.1-SNAPSHOT.jar
```

5. **Run Inventory Service** (in another terminal)

```bash
cd inventory
java -jar build/libs/inventory-0.0.1-SNAPSHOT.jar
```

**Note:**  
Make sure that `inventory/src/main/resources/application.properties` contains:

```
product.service.url=http://localhost:8081
```

---

You can now access:

- Product Swagger UI: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
- Inventory Swagger UI: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)

---

## Project Structure

```bash
shop/
├── product/                                # Product microservice
│   ├── src/main/java/com/link/product/
│   │   ├── application/                    # Application layer
│   │   │   ├── controller/                 # REST controllers
│   │   │   ├── dto/                        # Request/Response DTOs
│   │   │   └── mapper/                     # MapStruct mappers
│   │   ├── domain/                         # Domain layer
│   │   │   ├── model/                      # JPA entities
│   │   │   └── repository/                 # Spring Data repositories
│   │   ├── infrastructure/                 # Infrastructure layer
│   │   │   ├── config/                     # Configurations (Security, Swagger, etc.)
│   │   │   └── security/                   # API Key filter and security config
│   │   └── service/                        # Business services
│   ├── src/main/resources/
│   │   ├── application.properties          # Local profile
│   │   └── application-docker.properties   # Docker profile
│   └── Dockerfile
│
├── inventory/                              # Inventory microservice
│   ├── src/main/java/com/link/inventory/
│   │   ├── application/                    # Application layer (controllers, DTOs, mappers)
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   └── mapper/
│   │   ├── domain/                         # Hexagonal core / domain logic
│   │   │   ├── model/                      # Domain models (POJOs)
│   │   │   ├── api/                        # Input ports (service interfaces)
│   │   │   ├── spi/                        # Output ports (persistence, product client)
│   │   │   └── usecase/                    # Use cases (business logic)
│   │   └── infrastructure/                 # Infrastructure & adapters
│   │       ├── config/                     # App config (Resilience4j, WebClient, Swagger, Security)
│   │       ├── input/
│   │       │   └── rest/                   # REST controllers -> use cases
│   │       └── output/
│   │           ├── jpa/                    # JPA entities, repositories, adapters
│   │           └── webclient/              # Product API WebClient adapter
│   ├── src/main/resources/
│   │   ├── application.properties          # Local profile
│   │   └── application-docker.properties   # Docker profile
│   └── Dockerfile
│
├── docker-compose.yml                      # Orchestration for microservices and databases
├── settings.gradle.kts                     # Gradle multi-module settings/configuration
└── build.gradle.kts                        # Root Gradle build file (shared dependencies & plugins)
```

---

## API Endpoints

### Product Service ([http://localhost:8081](http://localhost:8081))

All responses follow the **JSON:API** specification.

| Method | Endpoint           | Description                                  |
|--------|--------------------|----------------------------------------------|
| POST   | `/product`        | Create a new product                         |
| GET    | `/product/{id}`   | Get product by ID                            |
| PUT    | `/product/{id}`   | Update product by ID                         |
| DELETE | `/product/{id}`   | Delete product by ID                         |
| GET    | `/product`        | List products (supports pagination)          |

**Pagination parameters** for `GET /product`:

- `page`: Page number (default: 0)
- `size`: Items per page (default: 10)
- `sortBy`: Field to sort by (default: `id`)
- `sortDirection`: `ASC` or `DESC` (default: `ASC`)

**Example request (create product):**
```json
{
  "productName": "Laptop Dell XPS 15",
  "description": "High-end professional laptop",
  "price": 5500000.00,
  "category": "Electronics",
  "brand": "Dell",
  "imageUrl": "https://example.com/laptop.jpg"
}
```

**Example JSON:API response:**
```json
{
  "data": {
    "type": "product",
    "id": "1",
    "attributes": {
      "productName": "Laptop Dell XPS 15",
      "description": "High-end professional laptop",
      "price": 5500000.00,
      "category": "Electronics",
      "brand": "Dell",
      "imageUrl": "https://example.com/laptop.jpg"
    },
    "links": {
      "self": "/product/1"
    }
  }
}
```

---

### Inventory Service ([http://localhost:8082](http://localhost:8082))

| Method | Endpoint                     | Description                                      |
|--------|------------------------------|--------------------------------------------------|
| POST   | `/inventory`               | Create inventory record for a product            |
| PUT    | `/inventory/{productId}`   | Update available quantity after a purchase       |

**Example request (create inventory record):**
```json
{
  "productId": 1,
  "quantity": 150,
  "location": "Warehouse A - Section 3"
}
```

**Example request (update quantity after purchase):**
```json
{
  "quantity": 10,
  "reason": "PURCHASE"
}
```

**Behavior:**
- When creating an inventory record, the service calls Product Service to validate that `productId` exists.
- When updating quantity, the service ensures the product exists and updates the record for that product.
- Inventory changes emit a log event.

**Error handling:**
- If validation fails (product not found or Product Service unavailable), errors are handled with retries and timeouts, and a JSON:API error response is returned.

**Example JSON:API error response:**
```json
{
  "errors": [
    {
      "status": "404",
      "title": "Product not found",
      "detail": "Product with ID 1 does not exist."
    }
  ]
}
```

---

**All endpoints (except health and Swagger UI) require the API Key header:**

```
X-API-Key: mi-clave-secreta-2025
```

See [API Key Authentication instructions above](#api-key-authentication).

---

## Technical Decisions

### 1. Hexagonal Architecture in Inventory Service

**Reason**

To isolate business logic from technical concerns (database, HTTP clients, frameworks). This makes the domain independent, testable and easier to evolve.

**Benefits**

- **Testability**: Use cases are pure Java, independent of Spring or JPA.
- **Maintainability**: Changing the database or HTTP client does not affect the core.
- **Flexibility**: Easy to replace JPA with another persistence technology, or WebClient with another HTTP client.
- **Clarity**: The domain expresses business rules instead of framework details.

**Structure**

```bash
Domain (Hexagon)
├── api/ → Input ports (what the domain exposes)
├── spi/ → Output ports (what the domain needs)
├── model/ → Pure domain models (POJOs)
└── usecase/ → Use cases (business logic)

Handler (Orchestator)
├── mappers/ → Transform data
├── dto/ → Transports data
├── handler/ → Orchestate Data and adds and extra layer to the arch

Infrastructure (Adapters)
├── input/rest/ → Primary adapters (REST → Domain)
└── output/
├── jpa/ → Secondary adapter (Domain → MySQL)
└── webclient/ → Secondary adapter (Domain → Product Service)
```

### 2. WebClient (Spring WebFlux) Between Services

**Reason**

Even though the project uses blocking style in the end, WebClient was chosen because:

- RestTemplate is deprecated in Spring and WebClient is the recommended option.
- WebClient is non-blocking by nature but still allows blocking usage via `.block()` for simplicity in this context.
- It integrates very well with Resilience4j (timeout, retry, circuit breaker).
- It provides fine-grained control for headers, timeouts and error handling.

**Example**
```java
Product product = webClient.get()
.uri("/product/{id}", productId)
.retrieve()
.bodyToMono(JsonApiProductResponse.class)
.block(); // Synchronous usage for this technical test
```

### 3. Resilience4j (Circuit Breaker, Retry, Timeout)

**Reason**

To keep the inventory service stable and responsive even when Product Service is failing or slow.

**Configuration concept**

- **Timeout**: Fail fast if Product Service does not respond within a given time.
- **Retry**: Retry a failing call a limited number of times with a wait duration.
- **Circuit Breaker**: Open the circuit after a certain failure rate to avoid hammering a failing service.

Typical configuration (conceptually):

Timeout: fail if Product does not respond in 2 seconds
```
#resilience4j.timelimiter.instances.productClient.timeoutDuration=2s
```

Retry: up to 3 attempts with 500ms wait
```
#resilience4j.retry.instances.productClient.maxAttempts=3
#resilience4j.retry.instances.productClient.waitDuration=500ms
```

Circuit Breaker: open after 50% failures, at least 5 calls
```
#resilience4j.circuitbreaker.instances.productClient.failureRateThreshold=50
#resilience4j.circuitbreaker.instances.productClient.minimumNumberOfCalls=5
#resilience4j.circuitbreaker.instances.productClient.waitDurationInOpenState=10s
```

Behavior summary:

1. If Product Service fails, the call is retried.
2. If calls are slow, the timeout aborts the call and triggers retries.
3. If many calls fail, the circuit opens and subsequent calls fail immediately for a period, protecting the system.

### 4. API Key Authentication

**Reason**

To provide simple, explicit security suitable for a technical test, without the overhead of OAuth2 or JWT.

**Implementation**

- Custom `ApiKeyAuthFilter` (extends `OncePerRequestFilter`) registered before `UsernamePasswordAuthenticationFilter`.
- Reads the `X-API-Key` header and compares it with a value configured in properties (`app.api.key`).
- If the key is missing or invalid, returns `401 Unauthorized`.
- CORS is configured to allow requests from `http://localhost:4200` (Angular frontend).
- Swagger UI and `/actuator/health` endpoints are excluded from the filter to be accessible without an API key.

### 5. JSON:API Standard

**Reason**

It was explicitly required in the technical test and helps standardize responses.

**Advantages**

- Predictable structure with `data`, `attributes`, `links`, `meta`.
- Built-in support for pagination and links.
- Consistent error format (`errors` array).
- Makes frontend consumption easier, particularly for lists and pagination.

Product service responses are wrapped using JSON:API, and errors are handled by a global exception handler that returns JSON:API-compliant error objects.

### 6. Docker Compose for Multiple Services

**Reason**

To be able to run:

- 2 MySQL databases
- 2 Spring Boot microservices

with a single command.

**Advantages**

- Each microservice has its own isolated database.
- Service discovery via container name (e.g. `product-service`).
- Reproducible environment for any reviewer: just needs Docker, no manual DB setup.
- Clear separation of concerns between local development and containerized environment using Spring profiles.

## Testing

### Strategy

Testing is applied at multiple layers across both microservices to ensure correctness, reliability, and maintainability.

---

### 1. **Unit Tests**

Unit tests cover:

- Service layer logic  
- Mappers  
- Validators  
- Use cases  

The tests follow the **Given | When | Then** approach.

Example (Inventory Use Case):

```java
@Test
void createInventory_shouldCallPersistenceAndProductClient() {
    Inventory inventory = new Inventory(...);
    Product mockProduct = new Product(...);

    when(productClientPort.getProductById(1L)).thenReturn(mockProduct);
    when(persistencePort.save(any())).thenReturn(inventory);

    Inventory result = inventoryUseCase.createInventory(inventory);

    verify(productClientPort).getProductById(1L);
    verify(persistencePort).save(any());
    assertEquals(inventory, result);
}
```

---

### 2. **Integration Tests**

Integration tests validate complete request–response flows, including controllers, services, repositories, database interactions, and JSON:API structures.

Key configurations:

- Dedicated \`test\` profile  
- H2 in-memory database for fast and isolated tests  
- \`@SpringBootTest\` + \`@AutoConfigureMockMvc\`  
- JSON:API response validation  

Example \`application-test.properties\`:

```
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=false
```

Example Integration Test (Product):

```java
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createProduct_shouldReturn201() throws Exception {
        String body = """
            {
              "productName": "Test Product",
              "description": "Test description",
              "price": 100000.0,
              "category": "Testing",
              "brand": "TestBrand",
              "imageUrl": "https://example.com/test.jpg"
            }
            """;

        mockMvc.perform(post("/product/")
                .contentType("application/json")
                .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.data.id").exists());
    }
}
```

---

### Future Work

Integration tests for the **Inventory** service are planned as a next step, but were not completed due to time constraints.

#### 3. Resilience Tests

- Simulate failures and timeouts from Product Service.
- Verify that the inventory use case:
  - Retries the call according to configuration.
  - Fails fast when the circuit breaker is open.
  - Returns meaningful error responses.

### How to Run Tests

From the root project directory, you can run all tests:

```bash
./gradlew test
```

To run tests only for the Product microservice:

```bash
./gradlew :product:test
```

To run tests only for the Inventory microservice:

```bash
./gradlew :inventory:test
```

(Optionally) To run tests and generate a coverage report using JaCoCo:

```bash
./gradlew test jacocoTestReport
```

---

Test and coverage reports are generated under:

- `product/build/reports/tests/test/index.html`
- `inventory/build/reports/tests/test/index.html`

## Diagrams

### High-Level Architecture

![microservice-arch](https://github.com/user-attachments/assets/f9e4a209-6262-44ac-8eaa-2656a26397d1)

---

### Data Flow: Create Inventory

Conceptual flow for `POST /inventory`:

```
Angular Frontend
   |
1. POST /inventory (JSON body + X-API-Key)
   v
Inventory Service (Port 8082)
   |
2. Validate API key
3. Call Product Service via WebClient:
   GET /product/{id} + X-API-Key
   v
Product Service (Port 8081)
   |
4. Read product from product_db (MySQL)
   v
Product DB (product_db)
   |
5. Return product data
   v
Inventory Service
   |
6. Apply business rules and create Inventory aggregate
7. Persist inventory in inventory_db
   v
Inventory DB (inventory_db)
   |
8. Return JSON:API response to client (201 Created)
   v
Angular Frontend
```

---

**Error and Resilience Flow (simplified):**

- If Product Service:
  - returns an error, or
  - times out
- Then:
  - Resilience4j retries the call up to N times (configurable).
  - If failure rate exceeds configuration, circuit breaker opens.
  - While circuit breaker is open, calls fail fast and a controlled JSON:API error response is returned to the client.

---

## Security

- API Key authentication via `X-API-Key` header.
- Global filter (`ApiKeyAuthFilter`) validates the key.
- CORS configuration allows the Angular frontend at `http://localhost:4200`.
- Public endpoints:
  - Swagger UI (`/swagger-ui.html`, `/v3/api-docs/**`)

No sensitive secrets are hardcoded; values are externalized to configuration files and environment variables (for Docker).

---

## Future Improvements

- Replace blocking `.block()` calls with a fully reactive pipeline (Mono/Flux).
- Add spring framework tools: admin, gateway, inverse proxy.
- Introduce an event-driven architecture (e.g. Kafka) to propagate product changes to inventory. Replace the event I send in logs!
- Implement integration tests to inventory based on product integration tests already developed.
- Set up CI/CD pipelines (e.g. GitHub Actions) Run Unit tests (Example).

---

## Author

- Name: \<Javier Santiago Salazar\>
- Email: \<javiersantiago45011@gmail.com\>
- LinkedIn: \<https://www.linkedin.com/in/javier-santiago-salazar\>





