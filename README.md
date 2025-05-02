# Saraha - Anonymous Messaging Application

**Project Overview**

Saraha is a Spring Boot-based web application that empowers users to send and receive anonymous messages. This platform prioritizes security, enabling users to register, log in, send anonymous messages, manage their message interactions (e.g., starring, blocking, deleting), and securely handle user authentication through JWT (JSON Web Tokens). The application adheres to a RESTful API design and leverages Spring Security for robust authentication and authorization mechanisms.

The project is architected with a clean structure, separating concerns into distinct layers for controllers, services, repositories, and Data Transfer Objects (DTOs). Key features include comprehensive message management, secure user authentication, and token-based authorization.

**Technologies Used**

* Java 17: The primary programming language.
* Spring Boot 3.x: The foundational framework for building the application.
* Spring Security: For managing authentication and authorization.
* JWT (JSON Web Tokens): Enabling secure user authentication and session management.
* Spring Data JPA: Facilitating database interactions.
* Hibernate: The Object-Relational Mapping (ORM) tool for mapping Java objects to database tables.
* MySQL: The chosen database system (configurable).
* Maven: For efficient dependency management.
* Lombok: Reducing boilerplate code, such as getters, setters, and constructors.
* Jakarta Validation: Ensuring the integrity of request data through validation.
* Mapper (e.g., MapStruct): For seamless conversion between entities and DTOs, maintaining a clear separation between the persistence layer and API responses.

**Features**

* User registration and login secured with JWT-based authentication.
* Ability to send anonymous messages to registered users.
* Retrieval of messages based on various criteria (all, unread, read, starred, unstarred).
* Comprehensive message management functionalities (star/unstar, block/unblock sender, delete).
* JWT token refresh mechanism for persistent session management.
* Secure process for users to change their passwords.
* Robust input validation for all incoming requests.

**Setup and Installation**

1.  **Clone the Repository:**
    ```bash
    git clone [https://github.com/your-username/saraha.git](https://github.com/your-username/saraha.git)
    cd saraha
    ```

2.  **Configure the Database:**
    Update `src/main/resources/application.properties` with your specific database configuration:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/saraha_db
    spring.datasource.username=your_username
    spring.datasource.password=your_password
    spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Configure JWT:**
    Set your JWT secret key and expiration time in `application.properties`:
    ```properties
    jwt.secret=your_jwt_secret_key
    jwt.expiration=86400000  # 24 hours in milliseconds
    ```

4.  **Build and Run:**
    ```bash
    mvn clean install
    mvn spring-boot:run
    ```
    The application will be accessible at `http://localhost:8080`.

5.  **Test the API:**
    Utilize Postman or any preferred API client to interact with the application's endpoints. Begin by using the `/api/v1/auth/register` endpoint to create a new user, followed by logging in via `/api/v1/auth/login` to obtain a JWT token.

**Authentication with JWT**

* **Registration and Login:** Users register and log in through the `/api/v1/auth/register` and `/api/v1/auth/login` endpoints, respectively. Upon successful login, a JWT token is provided in the response.
* **Authorization:** For accessing protected endpoints (e.g., those under `/saraha/*`), the JWT token must be included in the `Authorization` header in the `Bearer <token>` format.
* **Token Refresh:** The `/api/v1/auth/refresh-token` endpoint allows users to obtain a new JWT token before their current one expires.

**Endpoints**

### **Authentication Endpoints (/api/v1/auth)**

| Method | Endpoint        | Description             | Request Body                               | Response Body                           |
| :----- | :-------------- | :---------------------- | :----------------------------------------- | :-------------------------------------- |
| `POST` | `/register`     | Register a new user     | `RegisterRequest` (username, password)     | `AuthenticationResponse` (JWT token)    |
| `POST` | `/login`        | Log in an existing user | `AuthenticationRequest` (username, password) | `AuthenticationResponse` (JWT token)    |
| `POST` | `/refresh-token` | Refresh JWT token       | None (uses refresh token in cookies)       | Updated JWT token in response           |
| `POST` | `/change-password` | Change user password    | `ChangePasswordRequest` (oldPassword, newPassword) | `AuthenticationResponse` (JWT token)    |

### **Message Endpoints (/saraha)**

> All endpoints in this section require a valid JWT token in the `Authorization` header (`Bearer <token>`).

| Method | Endpoint                 | Description                          | Request Body | Response Body              |
| :----- | :----------------------- | :----------------------------------- | :----------- | :------------------------- |
| `POST` | `/send_anonymous`       | Send an anonymous message            | `MessageDTO` | `MessageDTO` (created message) |
| `GET`  | `/getAllUnreadMessages`  | Get all unread messages              | None         | List of `MessageDTO`       |
| `GET`  | `/getAllreadMessages`    | Get all read messages                | None         | List of `MessageDTO`       |
| `GET`  | `/getAllMessages`        | Get all messages (read and unread)   | None         | List of `MessageDTO`       |
| `GET`  | `/getMessage/{id}`       | Get a specific message by ID         | None         | `MessageDTO`               |
| `PUT`  | `/starMessage/{id}`      | Star a message                       | None         | Empty (HTTP 200)           |
| `PUT`  | `/unStarMessage/{id}`    | Unstar a message                     | None         | Empty (HTTP 200)           |
| `GET`  | `/getStarredMessages`    | Get all starred messages             | None         | List of `MessageDTO`       |
| `GET`  | `/getUnStarredMessages`  | Get all unstarred messages           | None         | List of `MessageDTO`       |
| `PUT`  | `/blockSenderOfMessage/{id}` | Block the sender of a message      | None         | Empty (HTTP 200)           |
| `PUT`  | `/unBlockSenderOfMessage/{id}` | Unblock the sender of a message    | None         | Empty (HTTP 200)           |
| `DELETE` | `/deleteMessage/{id}`     | Delete a message by ID               | None         | Empty (HTTP 200)           |

**DTOs and Mappers**

* **DTOs (Data Transfer Objects):** The application employs `MessageDTO` to transfer message data between the client and the server, ensuring a clear separation between the persistence layer (entities) and the API's data representation.
* **Mappers:** A mapping library (e.g., MapStruct) is utilized to facilitate the conversion between JPA entities (Message, User) and DTOs (`MessageDTO`). This practice prevents the internal structure of entities from being directly exposed through the API.

**Security**

* **Spring Security:** Used to secure the application's endpoints and manage authentication and authorization processes.
* **JWT:** Tokens are validated with each request to verify the user's authentication status.
* **Input Validation:** All request bodies are subject to validation using Jakarta Validation annotations (e.g., `@Valid`).

**Error Handling**

* The application defines a custom exception (`UserNotFoundException`) for specific error scenarios.
* Global exception handling is implemented using `@ControllerAdvice` to provide meaningful error responses to clients (e.g., 404 for resource not found, 500 for internal server errors).

**Future Improvements**

* Implement pagination for message retrieval endpoints to improve performance for large datasets.
* Introduce rate limiting for anonymous message sending to prevent abuse.
* Add email notifications to inform users of new messages.
* Enhance security by integrating CAPTCHA for anonymous message submissions.

**Contributing**

1.  Fork the repository on GitHub.
2.  Create a new branch for your contribution (`git checkout -b feature/your-feature`).
3.  Commit your changes (`git commit -m 'Add your feature'`).
4.  Push your branch to your fork (`git push origin feature/your-feature`).
5.  Submit a Pull Request through the GitHub interface.

**License**

This project is licensed under the MIT License - see the `LICENSE` file for complete details.
