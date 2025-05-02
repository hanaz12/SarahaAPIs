Saraha - Anonymous Messaging Application
Project Overview
Saraha is a Spring Boot-based web application that allows users to send and receive anonymous messages. The application provides a secure platform for users to register, log in, send anonymous messages, manage their messages (e.g., star, block, delete), and handle user authentication using JWT (JSON Web Tokens). It follows a RESTful API design and uses Spring Security for authentication and authorization.
The project is structured using a clean architecture with separate layers for controllers, services, repositories, and DTOs (Data Transfer Objects). It also includes features like message management, user authentication, and token-based authorization.
Technologies Used

Java 17: Programming language.
Spring Boot 3.x: Framework for building the application.
Spring Security: For authentication and authorization.
JWT (JSON Web Tokens): For secure user authentication and session management.
Spring Data JPA: For database operations.
Hibernate: ORM (Object-Relational Mapping) for mapping Java objects to database tables.
MySQL: Database (configurable).
Maven: Dependency management.
Lombok: To reduce boilerplate code (e.g., getters, setters, constructors).
Jakarta Validation: For request validation.
Mapper (e.g., MapStruct): To map between entities and DTOs, ensuring separation between the persistence layer and API responses.

Features

User registration and login with JWT-based authentication.
Send anonymous messages to registered users.
Retrieve messages (all messages, unread, read, starred, unstarred).
Manage messages (star/unstar, block/unblock sender, delete).
Refresh JWT tokens for session management.
Change user password securely.
Input validation for all requests.

Project Structure
saraha/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.spring/
│   │   │       ├── auth/               # Authentication-related classes (controller, service, requests)
│   │   │       ├── Controller/         # REST controllers for handling API requests
│   │   │       ├── DTO/                # Data Transfer Objects for API responses
│   │   │       ├── Exceptions/         # Custom exceptions
│   │   │       ├── Model/              # JPA entities
│   │   │       ├── Repository/         # JPA repositories for database operations
│   │   │       └── Service/            # Business logic layer
│   │   └── resources/
│   │       └── application.properties  # Configuration file (e.g., database, JWT settings)
└── pom.xml                             # Maven dependencies

Setup and Installation

Clone the Repository:
git clone https://github.com/your-username/saraha.git
cd saraha


Configure the Database:

Update src/main/resources/application.properties with your database configuration:spring.datasource.url=jdbc:mysql://localhost:3306/saraha_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update




Configure JWT:

Add your JWT secret and expiration settings in application.properties:jwt.secret=your_jwt_secret_key
jwt.expiration=86400000  # 24 hours in milliseconds




Build and Run:
mvn clean install
mvn spring-boot:run


The application will run on http://localhost:8080.


Test the API:

Use Postman or any API client to test the endpoints.
Start with the /api/v1/auth/register endpoint to create a user, then log in to get a JWT token.



Authentication with JWT
The application uses JWT for authentication:

Registration and Login: Users register and log in through the /api/v1/auth/register and /api/v1/auth/login endpoints. Upon successful login, a JWT token is returned in the response.
Authorization: The JWT token must be included in the Authorization header as Bearer <token> for all protected endpoints (e.g., /saraha/*).
Token Refresh: The /api/v1/auth/refresh-token endpoint allows users to refresh their JWT token before it expires.

Endpoints
Authentication Endpoints (/api/v1/auth)



Method
Endpoint
Description
Request Body
Response Body



POST
/register
Register a new user
RegisterRequest (username, password)
AuthenticationResponse (JWT token)


POST
/login
Log in an existing user
AuthenticationRequest (username, password)
AuthenticationResponse (JWT token)


POST
/refresh-token
Refresh JWT token
None (uses refresh token in cookies)
Updated JWT token in response


POST
/change-password
Change user password
ChangePasswordRequest (oldPassword, newPassword)
AuthenticationResponse (JWT token)


Message Endpoints (/saraha)
All endpoints in this section require a valid JWT token in the Authorization header (Bearer <token>).



Method
Endpoint
Description
Request Body
Response Body



POST
/send_anonymous
Send an anonymous message
MessageDTO
MessageDTO (created message)


GET
/getAllUnreadMessages
Get all unread messages
None
List of MessageDTO


GET
/getAllreadMessages
Get all read messages
None
List of MessageDTO


GET
/getAllMessages
Get all messages (read and unread)
None
List of MessageDTO


GET
/getMessage/{id}
Get a specific message by ID
None
MessageDTO


PUT
/starMessage/{id}
Star a message
None
Empty (HTTP 200)


PUT
/unStarMessage/{id}
Unstar a message
None
Empty (HTTP 200)


GET
/getStarredMessages
Get all starred messages
None
List of MessageDTO


GET
/getUnStarredMessages
Get all unstarred messages
None
List of MessageDTO


PUT
/blockSenderOfMessage/{id}
Block the sender of a message
None
Empty (HTTP 200)


PUT
/unBlockSenderOfMessage/{id}
Unblock the sender of a message
None
Empty (HTTP 200)


DELETE
/deleteMessage/{id}
Delete a message by ID
None
Empty (HTTP 200)


DTOs and Mappers

DTOs (Data Transfer Objects): The application uses MessageDTO to transfer message data between the client and server, ensuring separation between the persistence layer (entities) and API responses.
Mappers: The project uses a mapping library (e.g., MapStruct) to map between JPA entities (Message, User) and DTOs (MessageDTO). This ensures that the internal structure of entities is not exposed to the API.

Security

Spring Security: Used to secure endpoints and handle authentication/authorization.
JWT: Tokens are validated for each request to ensure the user is authenticated.
Input Validation: All request bodies are validated using Jakarta Validation annotations (e.g., @Valid).

Error Handling

The application uses a custom exception (UserNotFoundException) for specific cases.
Global exception handling is implemented using @ControllerAdvice to return meaningful error responses (e.g., 404 for not found, 500 for internal errors).

Future Improvements

Add pagination for message retrieval endpoints.
Implement rate limiting for anonymous message sending.
Add email notifications for new messages.
Enhance security with CAPTCHA for anonymous messages.
Add reporting for messages, users.
deactivate accounts.



License
This project is licensed under the MIT License - see the LICENSE file for details.
Contact
For any inquiries, please reach out to your-email@example.com.

