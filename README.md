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

## Setup and Installation

### Prerequisites
- Java 17 or higher
- Maven
- MySQL (or your preferred database)

### Step 1: Clone the Repository
```bash
git clone https://github.com/your-username/saraha.git
cd saraha

### Step 2: Configure the Database
```bash
Edit src/main/resources/application.properties:

propertiesspring.datasource.url=jdbc:mysql://localhost:3306/saraha_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
