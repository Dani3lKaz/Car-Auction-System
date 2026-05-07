# Car Auction Platform

A full-stack project designed for efficient vehicle bidding and auction management.

This repository contains the backend implementation for the Car Auction Platform. The backend is developed as a Spring Boot application with REST APIs, PostgreSQL persistence, and a layered architecture separating controllers, services, repositories, and DTOs.

### Technology Stack

- Java 25
- Spring Boot 4.0.3
- PostgreSQL
- JUnit 5 & Mockito for testing
- Maven for build and dependency management

### Project Structure

The backend source code is located in `backend/src/main/java/com/kazmierczak/daniel/car_auction_platform`.

Main packages:

- `config` - application configuration and initial data seeding
- `controller` - REST controllers exposing HTTP endpoints
- `dto` - data transfer objects used for request and response payloads
- `entity` - JPA entity classes mapped to database tables
- `exception` - custom exception handling
- `mapper` - mapping logic between entities and DTOs
- `repository` - Spring Data JPA repositories for persistence
- `service` - business logic and transaction handling

### Domain Model

The backend supports the following domain objects:

- `User` - platform users with balance, credentials, and versioning
- `Vehicle` - vehicles listed for auction
- `Auction` - auction listings referencing a vehicle and tracking price state
- `Bid` - user bids on auctions with timestamp and amount

DTO classes mirror the main aggregates and are used to transfer data through the REST API.

### REST API Endpoints

The backend exposes CRUD endpoints for all main aggregates:

- `GET /api/users` - list all users
- `GET /api/users/{userId}` - get user by id
- `POST /api/users` - create a user
- `PUT /api/users` - update a user
- `DELETE /api/users/{userId}` - delete a user

- `GET /api/vehicles` - list all vehicles
- `GET /api/vehicles/{vehicleId}` - get vehicle by id
- `POST /api/vehicles` - create a vehicle
- `PUT /api/vehicles` - update a vehicle
- `DELETE /api/vehicles/{vehicleId}` - delete a vehicle

- `GET /api/auctions` - list all auctions
- `GET /api/auctions/{auctionId}` - get auction by id
- `POST /api/auctions` - create an auction
- `PUT /api/auctions` - update an auction
- `DELETE /api/auctions/{auctionId}` - delete an auction

- `GET /api/bids` - list all bids
- `GET /api/bids/{bidId}` - get bid by id
- `POST /api/bids` - create a bid
- `PUT /api/bids` - update a bid
- `DELETE /api/bids/{bidId}` - delete a bid

### Database and Initialization

The backend uses PostgreSQL. The default connection is configured in `backend/src/main/resources/application.properties`:

- URL: `jdbc:postgresql://localhost:5432/auction_platform`
- Username: `admin`
- Password: `admin`

A PostgreSQL container configuration is available in `docker-compose.yml` with service `db` and initialization scripts mounted from `init-db`.

The application also contains a `DataSeeder` component that initializes test users and vehicles when the database is empty.

### Running the Backend

To start the backend locally:

1. Start PostgreSQL using Docker Compose:
   ```bash
   docker-compose up -d
   ```
2. Run the backend from the `backend` folder using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

Or build a jar and run it:

```bash
./mvnw clean package
java -jar backend/target/car-auction-platform-0.0.1-SNAPSHOT.jar
```

### Testing
The project follows a comprehensive testing strategy:
- Unit Tests: Business logic validation in the service layer.
- Web Layer Tests: Endpoint verification using `@WebMvcTest`.
To run all tests, use:
```bash
./mvnw test
```

### Notes

- The backend is designed for easy integration with a separate frontend application.
- Entities use optimistic locking through JPA `@Version`.
- The REST API uses JSON payloads and standard HTTP response codes.

