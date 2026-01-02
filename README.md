# 🍔 Food API - Restaurant Delivery System

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.8-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

A comprehensive REST API for food delivery management, built with Spring Boot following best practices of clean architecture, SOLID principles, and RESTful design patterns.

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Technologies](#-technologies)
- [Domain Model](#-domain-model)
- [Project Structure](#-project-structure)
- [Getting Started](#-getting-started)
- [API Documentation](#-api-documentation)
- [Authentication & Authorization](#-authentication--authorization)
- [API Versioning](#-api-versioning)
- [Available Endpoints](#-available-endpoints)
- [Email Notifications](#-email-notifications)
- [File Storage](#-file-storage)
- [Reports](#-reports)
- [Testing](#-testing)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### Core Features
- 🏪 **Restaurant Management**: Complete CRUD operations for restaurants with activation/deactivation controls
- 🍕 **Product Management**: Menu items with photos, prices, and availability status
- 📦 **Order Management**: Full order lifecycle (creation, confirmation, delivery, cancellation)
- 👥 **User Management**: User registration, authentication, and profile management
- 🔐 **OAuth2 Authentication**: Secure authentication using OAuth2 with JWT tokens
- 🔑 **Role-Based Access Control**: Fine-grained permissions and user groups
- 💳 **Payment Methods**: Multiple payment options management
- 🏙️ **Location Management**: Cities, states, and address handling
- 📊 **Reports**: Daily sales reports in PDF and JSON formats

### Advanced Features
- 🔗 **HATEOAS**: Hypermedia-driven API for better discoverability
- 📝 **API Documentation**: Complete OpenAPI 3.0 documentation with Swagger UI
- 📧 **Email Notifications**: Automated email notifications for order status changes
- 📁 **File Storage**: Support for local and AWS S3 storage
- 🔄 **API Versioning**: Support for multiple API versions (v1, v2)
- 🎯 **Caching**: Redis integration for improved performance
- 📈 **Monitoring**: Logging integration with Loggly
- 🛡️ **Security**: Spring Security with OAuth2 Resource Server

## 🏗 Architecture

This project follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                    API Layer (V1/V2)                     │
│  Controllers, DTOs, Assemblers, OpenAPI Documentation   │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│                    Domain Layer                          │
│     Entities, Services, Repositories, Events             │
└───────────────────────┬─────────────────────────────────┘
                        │
┌───────────────────────▼─────────────────────────────────┐
│               Infrastructure Layer                       │
│    JPA Implementation, Email, Storage, Security          │
└─────────────────────────────────────────────────────────┘
```

### Design Patterns Used
- **Repository Pattern**: Data access abstraction
- **DTO Pattern**: Data transfer and API decoupling
- **Assembler/Disassembler Pattern**: Object mapping and transformation
- **Service Layer Pattern**: Business logic encapsulation
- **Factory Pattern**: Object creation
- **Strategy Pattern**: Multiple storage implementations (Local/S3)
- **Event-Driven**: Domain events for loosely coupled components

## 🛠 Technologies

### Core
- **Java 17**: Latest LTS version with modern language features
- **Spring Boot 3.5.8**: Enterprise-grade framework
- **Spring Data JPA**: Data persistence abstraction
- **Hibernate**: ORM implementation
- **MySQL 8.0**: Relational database
- **Flyway**: Database migration management

### Security
- **Spring Security**: Authentication and authorization
- **OAuth2 Authorization Server**: Token-based authentication
- **OAuth2 Resource Server**: JWT token validation
- **Nimbus JOSE JWT**: JWT token handling

### API & Documentation
- **SpringDoc OpenAPI 3**: API documentation
- **Swagger UI**: Interactive API testing
- **Spring HATEOAS**: Hypermedia-driven REST API

### Storage & Files
- **AWS SDK S3**: Cloud file storage
- **JasperReports**: PDF report generation
- **FreeMarker**: Template engine for emails and reports

### Caching & Messaging
- **Spring Data Redis**: Caching layer
- **Spring Session**: Distributed session management
- **Spring Mail**: Email notifications

### Utilities
- **ModelMapper**: Object-to-object mapping
- **Lombok**: Boilerplate code reduction
- **Apache Commons Lang3**: Utility functions

### Testing
- **JUnit 5**: Unit testing framework
- **REST Assured**: API integration testing
- **Spring Boot Test**: Testing utilities

### Monitoring & Logging
- **Logback**: Logging framework
- **Loggly**: Cloud-based log management

## 📊 Domain Model

The system is built around the following main entities and their relationships:

![Class Diagram](public/models/ESR%20-%20Diagrama%20de%20classes.png)

### Main Entities

#### 🏪 Restaurant
- **Attributes**: name, shippingFee, active, open, registrationDate, updateDate
- **Relationships**: 
  - belongs to a Kitchen
  - has an Address (City/State)
  - has multiple Payment Methods
  - has multiple Products
  - has multiple Responsible Users

#### 🍕 Product
- **Attributes**: name, description, price, active
- **Relationships**: 
  - belongs to a Restaurant
  - has one Product Photo

#### 📦 Order
- **Attributes**: code (UUID), subtotal, shippingFee, totalValue, status
- **Status Flow**: CREATED → CONFIRMED → DELIVERED (or CANCELLED)
- **Relationships**: 
  - belongs to a Restaurant
  - belongs to a User (client)
  - has one Payment Method
  - has a delivery Address
  - has multiple Order Items

#### 👤 User
- **Attributes**: name, email, password, registrationDate
- **Relationships**: 
  - has multiple Groups
  - can be responsible for multiple Restaurants
  - can place multiple Orders

#### 🔐 Group
- **Attributes**: name
- **Relationships**: 
  - has multiple Permissions
  - has multiple Users

#### 🌆 Location Entities
- **State**: name
- **City**: name, belongs to a State
- **Address**: CEP, publicPlace, number, complement, neighborhood, city

#### 💳 Payment Method
- **Attributes**: description, updateDate
- **Relationships**: 
  - accepted by multiple Restaurants

## 📁 Project Structure

```
food-api/
├── src/
│   ├── main/
│   │   ├── java/com/gtech/food_api/
│   │   │   ├── api/                          # API Layer
│   │   │   │   ├── V1/                       # API Version 1
│   │   │   │   │   ├── controller/           # REST Controllers V1
│   │   │   │   │   ├── assembler/            # Entity to DTO converters
│   │   │   │   │   ├── disassembler/         # DTO to Entity converters
│   │   │   │   │   ├── dto/                  # Data Transfer Objects
│   │   │   │   │   └── utils/                # API utilities
│   │   │   │   └── V2/                       # API Version 2 (Enhanced)
│   │   │   │       ├── controller/           # REST Controllers V2
│   │   │   │       ├── openai/               # OpenAPI documentation
│   │   │   │       │   └── controller/       # API interface definitions
│   │   │   │       ├── assembler/            # Enhanced assemblers with HATEOAS
│   │   │   │       ├── disassembler/         # Enhanced disassemblers
│   │   │   │       ├── dto/                  # DTOs with OpenAPI schemas
│   │   │   │       │   ├── input/            # Input DTOs
│   │   │   │       │   ├── view/             # JSON view definitions
│   │   │   │       │   └── report/           # Report DTOs
│   │   │   │       └── utils/                # HATEOAS link builders
│   │   │   │
│   │   │   ├── core/                         # Core Configuration
│   │   │   │   ├── data/                     # Custom data configurations
│   │   │   │   ├── email/                    # Email configuration
│   │   │   │   ├── io/                       # I/O utilities (Base64)
│   │   │   │   ├── jackson/                  # JSON serialization config
│   │   │   │   ├── mapper/                   # ModelMapper configuration
│   │   │   │   ├── security/                 # Security configurations
│   │   │   │   │   ├── authorizationserver/  # OAuth2 Auth Server setup
│   │   │   │   │   └── resource/             # Resource Server setup
│   │   │   │   ├── springdoc/               # OpenAPI documentation config
│   │   │   │   ├── storage/                  # Storage configurations
│   │   │   │   ├── validation/               # Custom validators
│   │   │   │   └── web/                      # Web configuration
│   │   │   │
│   │   │   ├── domain/                       # Domain Layer
│   │   │   │   ├── event/                    # Domain events
│   │   │   │   ├── filter/                   # Query filters
│   │   │   │   ├── listener/                 # Event listeners
│   │   │   │   ├── model/                    # Domain entities
│   │   │   │   ├── repository/               # Repository interfaces
│   │   │   │   └── service/                  # Business services
│   │   │   │
│   │   │   ├── infra/                        # Infrastructure Layer
│   │   │   │   ├── repository/               # Repository implementations
│   │   │   │   └── service/                  # Infrastructure services
│   │   │   │       ├── email/                # Email service implementations
│   │   │   │       ├── query/                # Query services
│   │   │   │       ├── report/               # Report generation
│   │   │   │       └── storage/              # File storage implementations
│   │   │   │
│   │   │   └── FoodApiApplication.java       # Main application class
│   │   │
│   │   └── resources/
│   │       ├── application.properties         # Application configuration
│   │       ├── db/
│   │       │   ├── migration/                # Flyway migrations
│   │       │   └── data/                     # Seed data
│   │       ├── templates/                    # Email templates (FreeMarker)
│   │       │   ├── emails/                   # Email notification templates
│   │       │   └── pages/                    # OAuth2 pages
│   │       ├── reports/                      # JasperReports templates
│   │       ├── logback-spring.xml            # Logging configuration
│   │       └── messages.properties           # I18n messages
│   │
│   └── test/                                 # Test sources
│       ├── java/
│       └── resources/
│
├── public/
│   └── models/                               # Documentation and diagrams
│
├── pom.xml                                   # Maven dependencies
└── README.md                                 # This file
```

### Layer Responsibilities

#### 🎯 API Layer (`api/`)
- **Controllers**: Handle HTTP requests/responses
- **DTOs**: Define API contracts
- **Assemblers**: Convert entities to DTOs
- **Disassemblers**: Convert DTOs to entities
- **OpenAPI**: API documentation and contracts

#### 🏛 Domain Layer (`domain/`)
- **Entities**: Business objects and rules
- **Services**: Business logic implementation
- **Repositories**: Data access interfaces
- **Events**: Domain event definitions
- **Listeners**: Event handling logic

#### 🔧 Infrastructure Layer (`infra/`)
- **Repository Implementations**: JPA/Hibernate specifics
- **Email Services**: Email sending implementations
- **Storage Services**: File storage (Local/S3)
- **Query Services**: Complex database queries
- **Report Services**: PDF report generation

#### ⚙️ Core Layer (`core/`)
- **Configuration**: Application-wide configurations
- **Security**: Authentication and authorization setup
- **Validation**: Custom validation rules
- **Utilities**: Shared utilities and helpers

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
- **Maven 3.8+**
- **MySQL 8.0+**
- **Redis** (optional, for caching)
- **AWS Account** (optional, for S3 storage)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/gabriel-lima258/Restaurant-API.git
   cd Restaurant-API
   ```

2. **Configure the database**
   ```bash
   # Create MySQL database
   mysql -u root -p
   CREATE DATABASE food_api;
   ```

3. **Configure environment variables**
   
   Create a `.env` file or set the following environment variables:
   ```bash
   # Database
   MYSQL_PASSWORD=your_password
   
   # AWS S3 (optional)
   FOODAPI_STORAGE_S3_KEY_ID=your_aws_key
   FOODAPI_STORAGE_S3_SECRET_KEY=your_aws_secret
   
   # Email (AWS SES)
   SES_EMAIL_USERNAME=your_ses_username
   SES_EMAIL_PASSWORD=your_ses_password
   EMAIL_USERNAME=your_email@example.com
   
   # Logging (optional)
   LOGGLY_TOKEN=your_loggly_token
   ```

4. **Update `application.properties`**
   
   Edit `src/main/resources/application.properties`:
   ```properties
   # Database
   spring.datasource.url=jdbc:mysql://localhost:3306/food_api?createDatabaseIfNotExists=true&serverTimezone=UTC
   spring.datasource.username=root
   spring.datasource.password=${MYSQL_PASSWORD}
   
   # Storage (LOCAL or S3)
   foodapi.storage.type=LOCAL
   
   # Email (FAKE, SANDBOX, or SMTP)
   foodapi.email.type=FAKE
   ```

5. **Build the project**
   ```bash
   mvn clean install
   ```

6. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Database Migrations

Flyway automatically runs migrations on startup. Migration files are located in:
- `src/main/resources/db/migration/` - Schema migrations
- `src/main/resources/db/data/` - Seed data

### Initial Data

The application includes seed data that creates:
- Default users with different roles
- Sample restaurants and products
- States and cities
- Payment methods
- User groups and permissions

## 📚 API Documentation

### Swagger UI

Access the interactive API documentation at:
```
http://localhost:8080/swagger-ui.html
```

### OpenAPI Specification

Get the OpenAPI specification (JSON format) at:
```
http://localhost:8080/v3/api-docs
```

### API Entry Point (HATEOAS)

The API follows HATEOAS principles. Start exploring at:
```
GET http://localhost:8080/v2
```

Response example:
```json
{
  "_links": {
    "kitchens": { "href": "http://localhost:8080/v2/kitchens" },
    "restaurants": { "href": "http://localhost:8080/v2/restaurants" },
    "orders": { "href": "http://localhost:8080/v2/orders" },
    "users": { "href": "http://localhost:8080/v2/users" },
    "cities": { "href": "http://localhost:8080/v2/cities" },
    "states": { "href": "http://localhost:8080/v2/states" },
    "paymentMethods": { "href": "http://localhost:8080/v2/payment-methods" },
    "groups": { "href": "http://localhost:8080/v2/groups" },
    "permissions": { "href": "http://localhost:8080/v2/permissions" }
  }
}
```

## 🔐 Authentication & Authorization

### OAuth2 Flow

This API uses **OAuth2 Authorization Code with PKCE** flow.

#### 1. Authorization Request
```
GET http://localhost:8080/oauth2/authorize?
  response_type=code
  &client_id=food-api-users
  &redirect_uri=http://localhost:8080/callback
  &scope=READ WRITE
```

#### 2. Token Request
```bash
POST http://localhost:8080/oauth2/token
Content-Type: application/x-www-form-urlencoded

grant_type=authorization_code
&code={authorization_code}
&redirect_uri=http://localhost:8080/callback
&client_id=food-api-users
&client_secret=123456
```

#### 3. Access Protected Resources
```bash
GET http://localhost:8080/v2/restaurants
Authorization: Bearer {access_token}
```

### Swagger OAuth2

In Swagger UI, click **"Authorize"** and use:
- **Client ID**: `food-api-users`
- **Client Secret**: `123456`
- **Scopes**: `READ WRITE`

### User Roles & Permissions

The system implements fine-grained access control:

#### Default Groups
- **Administrators**: Full system access
- **Managers**: Restaurant and order management
- **Customers**: Order placement and viewing

#### Permission Examples
- `READ_RESTAURANTS` - View restaurant information
- `WRITE_RESTAURANTS` - Create/update restaurants
- `MANAGE_ORDERS` - Manage order lifecycle
- `READ_USERS_GROUPS_PERMISSIONS` - View users and groups

## 🔄 API Versioning

The API supports multiple versions for backward compatibility:

### Version 1 (V1)
- **Base Path**: `/v1/`
- **Features**: Basic CRUD operations
- **Example**: `GET /v1/restaurants`

### Version 2 (V2) - Current
- **Base Path**: `/v2/`
- **Features**: 
  - HATEOAS support
  - Enhanced DTOs with OpenAPI schemas
  - Better error handling
  - Complete API documentation
- **Example**: `GET /v2/restaurants`

## 📡 Available Endpoints

### 🏪 Restaurants
```
GET    /v2/restaurants              - List all restaurants
GET    /v2/restaurants/{id}         - Get restaurant by ID
POST   /v2/restaurants              - Create new restaurant
PUT    /v2/restaurants/{id}         - Update restaurant
DELETE /v2/restaurants/{id}         - Delete restaurant
PUT    /v2/restaurants/{id}/active  - Activate restaurant
DELETE /v2/restaurants/{id}/active  - Deactivate restaurant
PUT    /v2/restaurants/{id}/open    - Open restaurant
DELETE /v2/restaurants/{id}/open    - Close restaurant
```

### 🍕 Products
```
GET    /v2/restaurants/{restaurantId}/products              - List products
GET    /v2/restaurants/{restaurantId}/products/{productId}  - Get product
POST   /v2/restaurants/{restaurantId}/products              - Create product
PUT    /v2/restaurants/{restaurantId}/products/{productId}  - Update product
```

### 📸 Product Photos
```
GET    /v2/restaurants/{restaurantId}/products/{productId}/photo  - Get photo
PUT    /v2/restaurants/{restaurantId}/products/{productId}/photo  - Upload photo
DELETE /v2/restaurants/{restaurantId}/products/{productId}/photo  - Delete photo
```

### 📦 Orders
```
GET    /v2/orders                   - List orders (with filters)
GET    /v2/orders/{orderCode}       - Get order by code
POST   /v2/orders                   - Create new order
PUT    /v2/orders/{orderCode}/confirm  - Confirm order
PUT    /v2/orders/{orderCode}/deliver  - Mark as delivered
PUT    /v2/orders/{orderCode}/cancel   - Cancel order
```

### 👥 Users
```
GET    /v2/users            - List all users
GET    /v2/users/{id}       - Get user by ID
POST   /v2/users            - Register new user
PUT    /v2/users/{id}       - Update user
PUT    /v2/users/{id}/password  - Change password
```

### 🏙️ Cities & States
```
GET    /v2/cities           - List all cities
GET    /v2/cities/{id}      - Get city by ID
POST   /v2/cities           - Create new city
PUT    /v2/cities/{id}      - Update city
DELETE /v2/cities/{id}      - Delete city

GET    /v2/states           - List all states
GET    /v2/states/{id}      - Get state by ID
POST   /v2/states           - Create new state
PUT    /v2/states/{id}      - Update state
DELETE /v2/states/{id}      - Delete state
```

### 🍳 Kitchens
```
GET    /v2/kitchens         - List all kitchens (paginated)
GET    /v2/kitchens/{id}    - Get kitchen by ID
POST   /v2/kitchens         - Create new kitchen
PUT    /v2/kitchens/{id}    - Update kitchen
DELETE /v2/kitchens/{id}    - Delete kitchen
```

### 💳 Payment Methods
```
GET    /v2/payment-methods           - List all payment methods
GET    /v2/payment-methods/{id}      - Get payment method by ID
POST   /v2/payment-methods           - Create payment method
PUT    /v2/payment-methods/{id}      - Update payment method
DELETE /v2/payment-methods/{id}      - Delete payment method
```

### 🔐 Groups & Permissions
```
GET    /v2/groups                            - List all groups
GET    /v2/groups/{id}                       - Get group by ID
POST   /v2/groups                            - Create group
PUT    /v2/groups/{id}                       - Update group
DELETE /v2/groups/{id}                       - Delete group
GET    /v2/groups/{id}/permissions           - List group permissions
PUT    /v2/groups/{id}/permissions/{permId}  - Add permission
DELETE /v2/groups/{id}/permissions/{permId}  - Remove permission

GET    /v2/permissions                       - List all permissions
```

### 📊 Reports
```
GET /v2/reports/daily-sales?restaurantId={id}&creationDateStart={date}&creationDateEnd={date}
                            - Daily sales report (JSON)
GET /v2/reports/daily-sales/pdf?restaurantId={id}&creationDateStart={date}&creationDateEnd={date}
                            - Daily sales report (PDF)
```

## 📧 Email Notifications

The system sends automated emails for order events:

### Order Confirmed
- **Trigger**: When order status changes to CONFIRMED
- **Recipient**: Customer who placed the order
- **Content**: Order details, restaurant info, delivery address

### Order Cancelled
- **Trigger**: When order status changes to CANCELLED
- **Recipient**: Customer who placed the order
- **Content**: Cancellation notice with order details

### Order Delivered
- **Trigger**: When order status changes to DELIVERED
- **Recipient**: Customer who placed the order
- **Content**: Delivery confirmation

### Email Configuration Modes

1. **FAKE** - Logs emails to console (development)
2. **SANDBOX** - Sends to a fixed email address (testing)
3. **SMTP** - Sends via AWS SES (production)

Configure in `application.properties`:
```properties
foodapi.email.type=FAKE
```

## 📁 File Storage

Product photos can be stored in two ways:

### Local Storage
```properties
foodapi.storage.type=LOCAL
foodapi.storage.local.directory=/path/to/storage
```

### AWS S3 Storage
```properties
foodapi.storage.type=S3
foodapi.storage.s3.key-id=${AWS_KEY_ID}
foodapi.storage.s3.secret-key=${AWS_SECRET_KEY}
foodapi.storage.s3.bucket=your-bucket-name
foodapi.storage.s3.region=us-east-1
foodapi.storage.s3.directory=catalog
```

Supported formats: **JPEG, PNG** (max 1MB)

## 📊 Reports

### Daily Sales Report

Generate sales reports with flexible filtering:

#### JSON Format
```bash
GET /v2/reports/daily-sales
  ?restaurantId=1
  &creationDateStart=2025-01-01
  &creationDateEnd=2025-01-31
  &timeOffset=-03:00
```

Response:
```json
[
  {
    "date": "2025-01-01",
    "totalSales": 15,
    "totalBilling": 1250.00
  },
  {
    "date": "2025-01-02",
    "totalSales": 23,
    "totalBilling": 2100.50
  }
]
```

#### PDF Format
```bash
GET /v2/reports/daily-sales/pdf
  ?restaurantId=1
  &creationDateStart=2025-01-01
  &creationDateEnd=2025-01-31
  &timeOffset=-03:00
```

Returns a downloadable PDF file with formatted report.

## 🧪 Testing

### Run Unit Tests
```bash
mvn test
```

### Run Integration Tests
```bash
mvn verify
```

### Test Coverage
The project includes:
- Unit tests for services and utilities
- Integration tests for API endpoints
- REST Assured for API testing

### Manual Testing with Swagger UI

1. Access `http://localhost:8080/swagger-ui.html`
2. Click **"Authorize"**
3. Use OAuth2 authentication
4. Test endpoints interactively

### Example Test Requests

#### Register a New User
```bash
POST http://localhost:8080/v2/users
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "SecurePassword123!"
}
```

#### Create an Order
```bash
POST http://localhost:8080/v2/orders
Authorization: Bearer {token}
Content-Type: application/json

{
  "restaurant": { "id": 1 },
  "paymentMethod": { "id": 1 },
  "deliveryAddress": {
    "cep": "13000-000",
    "publicPlace": "Rua das Flores",
    "number": "123",
    "neighborhood": "Centro",
    "city": { "id": 1 }
  },
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "observation": "No onions"
    }
  ]
}
```

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

### Code Standards
- Follow Java conventions and best practices
- Write meaningful commit messages
- Include tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting PR

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](https://www.apache.org/licenses/LICENSE-2.0) file for details.

## 👥 Authors

- **Gabriel Lima** - [GitHub](https://github.com/gabriel-lima258)

## 🙏 Acknowledgments

- Spring Framework team for excellent documentation
- AlgaWorks for the comprehensive Spring Boot training
- The open-source community for inspiration and best practices

---

## 📞 Contact & Support

For questions, issues, or contributions:
- **GitHub Issues**: [Report a bug or request a feature](https://github.com/gabriel-lima258/Restaurant-API/issues)
- **GitHub Repository**: [https://github.com/gabriel-lima258/Restaurant-API](https://github.com/gabriel-lima258/Restaurant-API)



