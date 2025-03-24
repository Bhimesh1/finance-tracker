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


## Phase 2 Accomplishments

- Implemented core financial entity models (Accounts, Categories, Transactions)
- Created repository interfaces for database operations
- Developed DTOs for API request/response handling
- Implemented service layer with business logic
- Created RESTful controllers for entity operations
- Added automatic account balance updates based on transactions
- Implemented default category creation for new users

## Database Schema
The application now has the following main entities:

- **Users**: Store user information and authentication details
- **Accounts**: Different financial accounts owned by users
- **Categories**: Transaction categorization for better organization
- **Transactions**: Record of financial activities

## API Endpoints
Accounts

- `GET /api/accounts` - Get all accounts for the authenticated user
- `GET /api/accounts/{id}` - Get a specific account by ID
- `POST /api/accounts` - Create a new account
- `PUT /api/accounts/{id}` - Update an existing account
- `DELETE /api/accounts/{id}` - Delete an account

## Categories

- `GET /api/categories` - Get all categories for the authenticated user
- `GET /api/categories/type/{type}` - Get categories by type (INCOME or EXPENSE)
- `GET /api/categories/{id}` - Get a specific category by ID
- `POST /api/categories` - Create a new category
- `PUT /api/categories/{id}` - Update an existing category
- `DELETE /api/categories/{id}` - Delete a category

Transactions

- `GET /api/transactions` - Get paginated transactions for the authenticated user
- `GET /api/transactions/{id}` - Get a specific transaction by ID
- `GET /api/transactions/date-range` - Get transactions within a date range
- `GET /api/transactions/account/{accountId}` - Get transactions for a specific account
- `GET /api/transactions/category/{categoryId}` - Get transactions for a specific category
- `GET /api/transactions/filter` - Advanced filtering with multiple criteria
- `POST /api/transactions` - Create a new transaction
- `PUT /api/transactions/{id}` - Update an existing transaction
- `DELETE /api/transactions/{id}` - Delete a transaction

## Key Features Implemented

- Account Management: Create and manage different types of financial accounts
- Transaction Recording: Track income and expenses with detailed information
- Categorization: Organize transactions by custom categories
- Automatic Balance Updates: Account balances update automatically based on transactions
- Default Categories: New users get pre-populated categories for convenience
- Flexible Filtering: Advanced search and filtering options for transactions

## Security Features

- All endpoints (except authentication) require JWT authentication
- Users can only access their own data
- Security is enforced at both controller and service layers

## Next Steps (Phase 3)

- Implement budget creation and tracking
- Add financial goal setting and monitoring
- Create notification system for budget alerts
- Expand test coverage with integration tests


## Phase 3 Accomplishments

- Implemented budget creation and tracking for expense categories
- Added financial goal setting and progress monitoring
- Created notification system for budget alerts and goal deadlines
- Implemented scheduled tasks for monitoring financial status
- Enhanced transaction service to integrate with budget tracking

## New Features
### Budget Management

- Set monthly spending limits for expense categories
- Track actual spending against budgets
- View budget utilization percentage
- Receive alerts when approaching or exceeding budget limits

### Financial Goals

- Set savings targets with deadlines
- Track progress towards financial goals
- View percentage completion and days remaining
- Automatic status updates when goals are achieved or deadlines pass

### Notification System

- Budget alerts when spending reaches thresholds
- Goal deadline reminders and status updates
- Large transaction alerts
- System notifications
- Unread notification tracking

## API Endpoints
### Budgets

- `GET /api/budgets` - Get all budgets for the authenticated user
- `GET /api/budgets/period/{year}/{month}` - Get budgets for a specific period
- `GET /api/budgets/{id}` - Get a specific budget by ID
- `POST /api/budgets` - Create a new budget
- `PUT /api/budgets/{id}` - Update an existing budget
- `DELETE /api/budgets/{id}` - Delete a budget

### Financial Goals

- `GET /api/goals` - Get all financial goals for the authenticated user
- `GET /api/goals/status/{status}` - Get goals by status (IN_PROGRESS, ACHIEVED, FAILED)
- `GET /api/goals/{id}` - Get a specific goal by ID
- `POST /api/goals` - Create a new financial goal
- `PUT /api/goals/{id}` - Update an existing goal
- `PATCH /api/goals/{id}/progress` - Update goal progress
- `DELETE /api/goals/{id}` - Delete a goal

### Notifications

- `GET /api/notifications` - Get all notifications for the authenticated user
- `GET /api/notifications/unread` - Get unread notifications
- `GET /api/notifications/count` - Get count of unread notifications
- `PATCH /api/notifications/{id}/read` - Mark a notification as read
- `PATCH /api/notifications/read-all` - Mark all notifications as read

## Scheduled Tasks

- Daily budget checks to create notifications for budget thresholds
- Daily goal deadline checks to update goal status and create reminders
- Automatic updates for goals that have reached their deadlines

## Next Steps (Phase 4)

- Implement data analytics and reporting
- Add expense breakdown by category
- Create income vs expense tracking
- Develop monthly/yearly financial reports
- Implement visualization endpoints for charts and graphs





## Phase 4 Accomplishments
- Implemented comprehensive analytics and reporting features
- Created data visualization endpoints for charts and graphs
- Added expense breakdown by category analysis
- Developed cash flow analysis for income vs expense tracking
- Implemented monthly summary reports
- Created account balance history tracking
- Added data export capabilities for transactions and reports
- Developed dashboard summary endpoint for quick financial overview

## Analytics Features

### Expense Breakdown
- View expenses grouped by category
- Calculate percentage of total spending by category
- Filter by date range
- Sort by amount or percentage

### Cash Flow Analysis
- Monthly income vs expense comparison
- Breakdown of income and expenses by category
- Calculate net cash flow (savings)
- Visualize income and expense distribution

### Monthly Summary
- Track financial trends over time
- View income, expenses, and savings for multiple months
- Calculate savings rate as a percentage of income
- Identify patterns in financial behavior

### Account Balance History
- Track account balance changes over time
- Visualize balance trends
- Identify significant balance fluctuations

### Dashboard Summary
- Quick overview of financial status
- Current month's income, expenses, and savings
- Total balance across all accounts
- Active budgets and goals counts
- Most used transaction categories

## Export Features

### Transaction Export
- Export transactions to CSV format
- Filter by date range
- Include all transaction details

### Monthly Reports
- Generate comprehensive monthly financial reports
- Include summary metrics and transaction details
- Export to CSV format

### Custom Transaction Reports
- Create custom reports for specific date ranges
- Include summary information and transaction details
- Export to CSV format

## API Endpoints

### Analytics
- `GET /api/analytics/expenses-by-category` - Get expenses grouped by category
- `GET /api/analytics/cash-flow/{year}/{month}` - Get monthly cash flow analysis
- `GET /api/analytics/monthly-summary` - Get financial summary across multiple months
- `GET /api/analytics/account-balance-history/{accountId}` - Get account balance history
- `GET /api/analytics/dashboard-summary` - Get dashboard summary information

### Export
- `GET /api/export/transactions/csv` - Export transactions to CSV
- `GET /api/export/reports/monthly/{year}/{month}` - Generate monthly report
- `GET /api/export/reports/transactions` - Generate custom transaction report

## Visualization Support
The analytics endpoints are designed to provide data in formats suitable for:
- Pie charts (expense breakdown)
- Bar charts (cash flow analysis)
- Line charts (monthly summary, account balance history)
- Dashboard widgets (summary information)

## Next Steps
The application now has a complete set of features for personal finance management:
- User authentication and management
- Account, transaction, and category management
- Budget planning and tracking
- Financial goal setting and monitoring
- Notification system
- Analytics and reporting

Future enhancements could include:
- Mobile application development
- Advanced forecasting and predictive analytics
- Bill payment and scheduling features
- Investment tracking and analysis
- Multi-currency support
- Third-party financial service integrations






