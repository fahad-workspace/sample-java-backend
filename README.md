# Movie Database API

A comprehensive REST API for managing movies, actors, directors, and their relationships. Built with Spring Boot 3.4 and Java 21.

## Features

- **Complete CRUD Operations**: Manage movies, actors, directors, and character roles
- **Search Capabilities**: Search movies by title or genre, actors and directors by name, roles by character name
- **Pagination Support**: For large result sets (actors)
- **Full API Documentation**: Using Swagger/OpenAPI 3
- **Data Validation**: Input validation using Jakarta Bean Validation
- **H2 In-Memory Database**: For easy setup and testing
- **Comprehensive Test Suite**: Unit tests for controllers, services, mappers

## Technology Stack

- **Java 21**
- **Spring Boot 3.4.4**
- **Spring Data JPA**
- **H2 Database**
- **Lombok**
- **SpringDoc OpenAPI UI**
- **JUnit 5**
- **Mockito**

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 21 or higher
- Maven 3.6 or higher

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/movie-database-api.git
   cd movie-database-api
   ```

2. Build the project:
   ```bash
   mvn clean package
   ```

3. Run the application:
   ```bash
   java -jar target/sample-java-backend-0.0.1-SNAPSHOT.jar
   ```

Or run it using Maven:

```bash
mvn spring-boot:run
```

The application will start on http://localhost:8080

### Database

The application uses an in-memory H2 database that is pre-loaded with sample data on startup. You can access the H2 console at:

```
http://localhost:8080/h2-console
```

Connection details:

- JDBC URL: `jdbc:h2:mem:moviedb`
- Username: `sa`
- Password: `password`

## API Documentation

Once the application is running, you can access the OpenAPI documentation at:

```
http://localhost:8080/swagger-ui.html
```

This provides a complete interactive documentation of all available endpoints.

### API Endpoints

#### Movies

- `GET /api/movies` - Get all movies
- `GET /api/movies/{id}` - Get movie by ID
- `GET /api/movies/search?title={title}&genre={genre}` - Search movies by title or genre
- `POST /api/movies` - Create a new movie
- `PUT /api/movies/{id}` - Update a movie
- `PATCH /api/movies/{id}` - Partially update a movie
- `DELETE /api/movies/{id}` - Delete a movie

#### Actors

- `GET /api/actors` - Get all actors (paginated)
- `GET /api/actors/{id}` - Get actor by ID
- `GET /api/actors/search?name={name}` - Search actors by name
- `POST /api/actors` - Create a new actor
- `PUT /api/actors/{id}` - Update an actor
- `DELETE /api/actors/{id}` - Delete an actor

#### Directors

- `GET /api/directors` - Get all directors
- `GET /api/directors/{id}` - Get director by ID
- `GET /api/directors/search?name={name}` - Search directors by name
- `POST /api/directors` - Create a new director
- `PUT /api/directors/{id}` - Update a director
- `DELETE /api/directors/{id}` - Delete a director

#### Roles

- `GET /api/roles` - Get all character roles
- `GET /api/roles/{id}` - Get role by ID
- `GET /api/roles/movie/{movieId}` - Get all roles in a movie
- `GET /api/roles/actor/{actorId}` - Get all roles played by an actor
- `GET /api/roles/search?characterName={name}` - Search roles by character name
- `POST /api/roles` - Create a new role
- `PUT /api/roles/{id}` - Update a role
- `DELETE /api/roles/{id}` - Delete a role

## Project Structure

```
src/main/java/com/sample/backend/
├── config/           # Application configuration
├── controller/       # REST controllers
├── dto/              # Data Transfer Objects
├── exception/        # Exception handling
├── mapper/           # Entity-DTO mappers
├── model/            # JPA entities
├── repository/       # Spring Data repositories
└── service/          # Business logic
```

## Running Tests

```bash
mvn test
```

## Performance Optimizations

The API includes several optimizations:

- Entity graph specifications for fetching related entities
- Batch fetching for collections
- Pagination for large result sets
- Spring Data JPA query methods for efficient filtering

