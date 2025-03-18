# Personal Finance Tracker - Phase 1

## Project Overview
The Personal Finance Tracker is a Spring Boot application designed to help users manage their financial activities, track expenses, create budgets, and analyze their spending habits.

## Phase 1 Accomplishments
- Basic Spring Boot project setup
- Database configuration with PostgreSQL
- User registration and authentication system with JWT
- Security configuration
- Docker setup for containerization
- Initial CI/CD pipeline with GitHub Actions

## Project Structure
    src/main/java/com/personalfinance/finance_tracker/

        ├── config/              # Configuration classes
        ├── controller/          # REST controllers
        ├── dto/                 # Data Transfer Objects
        │   ├── request/         # Request DTOs
        │   └── response/        # Response DTOs
        ├── exception/           # Custom exceptions
        ├── model/               # Entity classes
        ├── repository/          # Spring Data JPA repositories
        ├── security/            # Security related classes
        │   └── jwt/             # JWT implementation
        ├── service/             # Business logic layer
        │   └── impl/            # Service implementations
        └── util/                # Utility classes


## Technologies Used
- Java 17
- Spring Boot 3.x
- Spring Security with JWT
- Spring Data JPA
- PostgreSQL
- Docker & Docker Compose
- Maven
- GitHub Actions for CI/CD

## How to Run
1. Clone the repository
2. Make sure you have Docker and Docker Compose installed
3. Run `docker-compose up` to start the application and database
4. Access the application at http://localhost:8080

## API Endpoints
- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/signin` - Authenticate and get JWT token
- `GET /api/test/all` - Public content (test endpoint)
- `GET /api/test/user` - Protected content (requires authentication)

## Next Steps (Phase 2)
- Implement account management functionality
- Create transaction recording system
- Add category management for transactions
- Implement basic reporting features











