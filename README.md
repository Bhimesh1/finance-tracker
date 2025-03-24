# Personal Finance Tracker

A comprehensive financial management application built with Spring Boot that helps users track finances, set budgets, establish financial goals, and gain insights through analytics.

## Project Motivation

The Personal Finance Tracker addresses fundamental challenges in personal financial management:

- **Financial Literacy Gap**: Makes financial tracking accessible with intuitive tools
- **Comprehensive Integration**: Combines budgeting, expense tracking, and goal setting in one platform
- **Data Privacy**: Secure, self-hosted solution for sensitive financial data
- **Personalization**: Customizable tools that adapt to individual financial situations

## Features

### User Management
- Secure registration and authentication with JWT
- Role-based access control

### Account Management
- Support for multiple account types (checking, savings, credit cards, investments)
- Automatic balance tracking

### Transaction Tracking
- Record income, expenses, and transfers
- Custom categorization
- Advanced filtering and search capabilities

### Budget Planning
- Set monthly budgets by category
- Real-time tracking against budgets
- Visual progress indicators

### Financial Goals
- Create saving targets with deadlines
- Track progress and completion status
- Automatic status updates

### Notification System
- Budget threshold alerts
- Goal deadline reminders
- Large transaction notifications
- Unread notification tracking

### Analytics & Reporting
- Expense breakdown by category
- Income vs expense analysis
- Monthly financial summaries
- Account balance history
- Export capabilities (CSV)

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Security**: Spring Security, JWT
- **Database**: PostgreSQL, JPA/Hibernate
- **Build Tool**: Maven
- **Containerization**: Docker, Docker Compose
- **CI/CD**: GitHub Actions

## Project Structure

```
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
```

## API Endpoints

### Authentication
- `POST /api/auth/signup` - Register a new user
- `POST /api/auth/signin` - Authenticate and get JWT token

### Accounts
- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{id}` - Get account by ID
- `POST /api/accounts` - Create a new account
- `PUT /api/accounts/{id}` - Update an account
- `DELETE /api/accounts/{id}` - Delete an account

### Categories
- `GET /api/categories` - Get all categories
- `GET /api/categories/type/{type}` - Get categories by type
- `GET /api/categories/{id}` - Get category by ID
- `POST /api/categories` - Create a new category
- `PUT /api/categories/{id}` - Update a category
- `DELETE /api/categories/{id}` - Delete a category

### Transactions
- `GET /api/transactions` - Get paginated transactions
- `GET /api/transactions/{id}` - Get transaction by ID
- `GET /api/transactions/date-range` - Get transactions by date range
- `GET /api/transactions/account/{accountId}` - Get transactions by account
- `GET /api/transactions/category/{categoryId}` - Get transactions by category
- `GET /api/transactions/filter` - Advanced filtering
- `POST /api/transactions` - Create a new transaction
- `PUT /api/transactions/{id}` - Update a transaction
- `DELETE /api/transactions/{id}` - Delete a transaction

### Budgets
- `GET /api/budgets` - Get all budgets
- `GET /api/budgets/period/{year}/{month}` - Get budgets for a period
- `GET /api/budgets/{id}` - Get budget by ID
- `POST /api/budgets` - Create a new budget
- `PUT /api/budgets/{id}` - Update a budget
- `DELETE /api/budgets/{id}` - Delete a budget

### Financial Goals
- `GET /api/goals` - Get all financial goals
- `GET /api/goals/status/{status}` - Get goals by status
- `GET /api/goals/{id}` - Get goal by ID
- `POST /api/goals` - Create a new goal
- `PUT /api/goals/{id}` - Update a goal
- `PATCH /api/goals/{id}/progress` - Update goal progress
- `DELETE /api/goals/{id}` - Delete a goal

### Notifications
- `GET /api/notifications` - Get all notifications
- `GET /api/notifications/unread` - Get unread notifications
- `GET /api/notifications/count` - Get unread count
- `PATCH /api/notifications/{id}/read` - Mark notification as read
- `PATCH /api/notifications/read-all` - Mark all as read

### Analytics
- `GET /api/analytics/expenses-by-category` - Get expenses by category
- `GET /api/analytics/cash-flow/{year}/{month}` - Get monthly cash flow
- `GET /api/analytics/monthly-summary` - Get financial summary across months
- `GET /api/analytics/account-balance-history/{accountId}` - Get balance history
- `GET /api/analytics/dashboard-summary` - Get dashboard summary

### Export
- `GET /api/export/transactions/csv` - Export transactions to CSV
- `GET /api/export/reports/monthly/{year}/{month}` - Generate monthly report
- `GET /api/export/reports/transactions` - Generate transaction report

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven
- Docker and Docker Compose (optional)
- PostgreSQL (if not using Docker)

### Running with Docker (Recommended)
1. Clone the repository
```
git clone https://github.com/yourusername/finance-tracker.git
cd finance-tracker
```

2. Start the application with Docker Compose
```
docker-compose up --build
```

3. Access the application at http://localhost:8080

### Running Locally
1. Clone the repository
```
git clone https://github.com/yourusername/finance-tracker.git
cd finance-tracker
```

2. Configure database connection in `src/main/resources/application.properties`

3. Build and run the application
```
mvn clean install
mvn spring-boot:run
```

4. Access the application at http://localhost:8080

## Testing the API

1. Register a user
```
POST http://localhost:8080/api/auth/signup
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

2. Login to get a token
```
POST http://localhost:8080/api/auth/signin
{
  "email": "john@example.com",
  "password": "password123"
}
```

3. Use the token for authenticated requests
```
Authorization: Bearer <your-jwt-token>
```

## Development

The project was developed in four phases:

1. **Phase 1**: Core setup and user management
2. **Phase 2**: Core financial entities
3. **Phase 3**: Budget and financial goals
4. **Phase 4**: Analytics and reporting

## Future Enhancements

- Frontend development (React, Angular, or Vue.js)
- Mobile application
- Advanced forecasting and predictive analytics
- Bill payment scheduling
- Investment portfolio tracking
- Multi-currency support
- Third-party financial service integrations

## License

This project is licensed under the MIT License - see the LICENSE file for details

## Acknowledgments

- Spring Boot and the Spring team
- PostgreSQL
- Docker