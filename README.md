# Saraha - Anonymous Messaging Application

Saraha is a Spring Boot-based web application that allows users to send and receive anonymous messages. The application provides a secure platform for users to register, log in, send anonymous messages, manage their messages (e.g., star, block, delete), and handle user authentication using JWT (JSON Web Tokens).

## Features

- **User Management**: Registration, login with JWT-based authentication
- **Messaging System**: Send anonymous messages to registered users
- **Message Management**: 
  - Retrieve messages (all, unread, read, starred, unstarred)
  - Star/unstar messages
  - Block/unblock senders
  - Delete messages
- **Security**: JWT token refresh and secure password changes
- **Input Validation**: For all requests

## Technologies Used

- **Java 17**: Core programming language
- **Spring Boot 3.x**: Application framework
- **Spring Security**: Authentication and authorization
- **JWT (JSON Web Tokens)**: Secure user authentication
- **Spring Data JPA**: Database operations
- **Hibernate**: ORM for database mapping
- **MySQL**: Primary database (configurable)
- **Maven**: Dependency management
- **Lombok**: Reduces boilerplate code
- **Jakarta Validation**: Request validation
- **MapStruct**: Entity to DTO mapping

## Project Structure
saraha/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.spring/
│   │   │       ├── auth/           # Authentication-related classes
│   │   │       ├── Controller/     # REST controllers
│   │   │       ├── DTO/            # Data Transfer Objects
│   │   │       ├── Exceptions/     # Custom exceptions
│   │   │       ├── Model/          # JPA entities
│   │   │       ├── Repository/     # JPA repositories
│   │   │       └── Service/        # Business logic layer
│   │   └── resources/
│   │       └── application.properties  # Configuration file
└── pom.xml                             # Maven dependencies
