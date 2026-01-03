# User CRUD API

A Spring Boot application that provides CRUD operations for user management with MySQL database.

## Features

- ✅ Full CRUD operations (Create, Read, Update, Delete)
- ✅ MySQL database integration
- ✅ Health check endpoint
- ✅ Input validation
- ✅ Exception handling
- ✅ Comprehensive unit tests
- ✅ Docker support
- ✅ Docker Compose for easy deployment

## Technology Stack

- **Java 21**
- **Spring Boot 3.4.3**
- **Spring Data JPA**
- **MySQL 8.0**
- **H2 Database** (for testing)
- **Maven**
- **Docker & Docker Compose**
- **JUnit 5 & Mockito** (for testing)

## Prerequisites

- Java 21 or higher
- Maven 3.6+
- MySQL 8.0+ (or use Docker)
- Docker & Docker Compose (optional, for containerized deployment)

## Getting Started

### 1. Clone the repository

```bash
cd C:\codebase\local\user-crud-api
```

### 2. Configure MySQL Database

Update `src/main/resources/application.yml` with your MySQL credentials (or use environment variables):

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/userdb?createDatabaseIfNotExist=true
    username: root
    password: rootpassword
```

### 3. Build the application

```bash
mvn clean package
```

### 4. Run the application

```bash
java -jar target/user-crud-api-1.0.0.jar
```

The application will start on `http://localhost:8080`

## Running with Docker Compose

The easiest way to run the application with MySQL:

```bash
docker-compose up --build
```

This will:
- Start a MySQL 8.0 container
- Build and start the Spring Boot application
- Configure networking between containers

## API Endpoints

### Health Check
- **GET** `/api/health` - Check application and database health

### User Operations
- **POST** `/api/users` - Create a new user
- **GET** `/api/users` - Get all users
- **GET** `/api/users/{id}` - Get user by ID
- **PUT** `/api/users/{id}` - Update user
- **DELETE** `/api/users/{id}` - Delete user

### Example Request (Create User)

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "phone": "1234567890"
  }'
```

### Example Request (Get All Users)

```bash
curl http://localhost:8080/api/users
```

### Example Request (Health Check)

```bash
curl http://localhost:8080/api/health
```

## Testing

Run all tests:

```bash
mvn test
```

### Test Coverage

- **Unit Tests**: Service layer with Mockito
- **Repository Tests**: JPA repository with H2 in-memory database
- **Controller Tests**: MockMvc for REST API testing
- **Integration Tests**: Application context and configuration tests
- **Health Check Tests**: Database connection verification

## Project Structure

```
user-crud-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/usercrud/
│   │   │   ├── UserCrudApiApplication.java
│   │   │   ├── controller/
│   │   │   │   ├── HealthCheckController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/
│   │   │   │   └── UserDTO.java
│   │   │   ├── entity/
│   │   │   │   └── User.java
│   │   │   ├── exception/
│   │   │   │   ├── DuplicateEmailException.java
│   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java
│   │   │   └── service/
│   │   │       └── UserService.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       ├── java/com/example/usercrud/
│       │   ├── UserCrudApiApplicationTest.java
│       │   ├── controller/
│       │   │   ├── HealthCheckControllerTest.java
│       │   │   └── UserControllerTest.java
│       │   ├── repository/
│       │   │   └── UserRepositoryTest.java
│       │   └── service/
│       │       └── UserServiceTest.java
│       └── resources/
│           └── application-test.yml
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## Environment Variables

You can configure the application using environment variables:

- `DB_HOST` - MySQL host (default: localhost)
- `DB_PORT` - MySQL port (default: 3306)
- `DB_NAME` - Database name (default: userdb)
- `DB_USERNAME` - Database username (default: root)
- `DB_PASSWORD` - Database password (default: rootpassword)

## Docker Build

Build the Docker image manually:

```bash
docker build -t user-crud-api .
```

Run the container:

```bash
docker run -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_USERNAME=root \
  -e DB_PASSWORD=rootpassword \
  user-crud-api
```

## Stopping the Application

If running with Docker Compose:

```bash
docker-compose down
```

To remove volumes:

```bash
docker-compose down -v
```

## License

This project is open source and available under the MIT License.
## CI Pipeline

This project uses GitHub Actions for Continuous Integration.

On every push or pull request to `main`, the pipeline:
- Checks out the code
- Sets up Java 21
- Runs Maven tests
- Builds the application

CI status can be viewed in the GitHub Actions tab.
