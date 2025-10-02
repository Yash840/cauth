# Cross Auth

![Java](https://img.shields.io/badge/Java-25-blue?logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-12%2B-blue?logo=postgresql)
![Redis](https://img.shields.io/badge/Redis-7%2B-red?logo=redis)
![Build](https://img.shields.io/badge/build-passing-success)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

---

# 🚀 Cross Auth

A secure, multi-tenant authentication system built with Spring Boot, Redis, and PostgreSQL. Provides robust user and application management, JWT authentication, Argon2 password hashing, and email verification.

---

## ✨ Features

### 🔐 Security
- **Argon2 Password Hashing** – Industry-standard encryption
- **JWT Authentication** – Per-app secrets, expiration, stateless sessions
- **Redis Integration** – Fast, secure temporary code storage
- **Multi-tenant Architecture** – Isolated user management per application
- **Secure ID Generation** – Cryptographically secure random IDs
- **Email Verification** – Built-in workflow
- **Password Management** – Change/reset password

### 👥 User Management
- User registration & authentication
- Profile management (email, phone)
- Email verification status
- User statistics & analytics
- Bulk user operations per app

### 📱 Application Management
- Application registration & management
- App-specific user isolation
- Dynamic app updates
- App statistics & monitoring

### 🛡️ API Security
- Global exception handling
- Input validation (Bean Validation)
- Structured error responses
- RESTful API design

---

## 🏗️ Architecture

**Tech Stack:**
- Java 25
- Spring Boot 3.5.6
- Spring Data JPA
- Spring Security
- Spring Validation
- Redis
- PostgreSQL
- Lombok
- Maven

**Project Structure:**
```
src/main/java/org/cross/cauth/
├── Application/         # App logic
├── User/                # User logic
├── Exception/           # Global error handling
├── Jwt/                 # JWT services
├── EmailService/        # Email sending
├── ClientAuthService/   # Client auth endpoints
├── config/              # Security/config
├── utils/               # Utilities (Argon2, Redis, ID gen)
└── CrossAuthApplication.java
```

---

## 🚦 Getting Started

### Prerequisites
- Java 25+
- Maven 3.6+
- PostgreSQL 12+
- Redis 7+
- Git

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd cauth
   ```
2. **Set up environment variables**
   Create a `.env` file in the root directory:
   ```env
   DB_URL=jdbc:postgresql://localhost:5432/crossauth
   DB_USER=your_db_username
   DB_PASSWORD=your_db_password
   REDIS_HOST=localhost
   REDIS_PORT=6379
   SPRING_MAIL_USERNAME=your_email@example.com
   SPRING_MAIL_PASSWORD=your_email_password
   APP_JWT_SECRET=your_jwt_secret
   APP_JWT_EXPIRATION=3600000
   ```
3. **Database setup**
   ```sql
   CREATE DATABASE crossauth;
   ```
4. **Build the project**
   ```bash
   mvn clean install
   ```
5. **Run the application**
   ```bash
   mvn spring-boot:run
   ```
   The app starts at `http://localhost:8085`

---

## 📊 Database Schema

### Applications Table
- `id` (UUID) – Primary key
- `app_id` (String) – Unique app ID
- `app_name` (String) – Name
- `owner_email` (String) – Owner
- `app_secret` (String) – Secret key
- `created_at` (LocalDateTime)
- `updated_at` (LocalDateTime)

### Users Table
- `id` (Long) – Primary key
- `auth_id` (String) – Unique auth ID
- `email` (String)
- `app_id` (String) – App association
- `hashed_password` (String) – Argon2 hash
- `is_email_verified` (Boolean)
- `phone_number` (String)
- `created_at` (LocalDateTime)
- `updated_at` (LocalDateTime)

---

## 🔌 API Endpoints

### Application Management
- `GET /api/v1/apps` – List apps
- `GET /api/v1/apps/{appId}` – Get app by ID
- `POST /api/v1/apps` – Register app
- `PATCH /api/v1/apps/{appId}` – Update app
- `DELETE /api/v1/apps/{appId}` – Delete app

### User Management
- `GET /api/v1/users` – List users
- `GET /api/v1/users/{id}` – Get user by ID
- `GET /api/v1/users/auth/{authId}` – Get user by auth ID
- `GET /api/v1/users/email?email={email}&appId={appId}` – Get user by email/app
- `POST /api/v1/users` – Create user
- `PUT /api/v1/users/auth/{authId}` – Update user
- `DELETE /api/v1/users/auth/{authId}` – Delete user

### Authentication
- `POST /api/v1/users/authenticate` – Login
- `PATCH /api/v1/users/auth/{authId}/verify-email` – Verify email
- `PATCH /api/v1/users/auth/{authId}/change-password` – Change password
- `PATCH /api/v1/users/auth/{authId}/reset-password` – Reset password

### User Queries
- `GET /api/v1/users/exists?email={email}&appId={appId}` – Check user exists
- `GET /api/v1/users/app/{appId}` – Users by app
- `GET /api/v1/users/unverified` – Unverified users
- `GET /api/v1/users/count` – Total user count
- `GET /api/v1/users/count/app/{appId}` – User count by app

---

## 📝 API Examples

### Register New Application
```bash
curl -X POST http://localhost:8085/api/v1/apps \
  -H "Content-Type: application/json" \
  -d '{
    "appName": "My App",
    "ownerEmail": "owner@example.com",
    "description": "My appId description"
  }'
```

### Create New User
```bash
curl -X POST http://localhost:8085/api/v1/users \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "appId": "APP.20250921123456.ABC12345",
    "password": "securePassword123",
    "phoneNumber": "+1234567890"
  }'
```

### Authenticate User
```bash
curl -X POST http://localhost:8085/api/v1/users/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "appId": "APP.20250921123456.ABC12345",
    "password": "securePassword123"
  }'
```

---

## 🔧 Configuration

### Application Properties
```properties
# Server
server.port=8085

# Database
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver

# Redis
spring.redis.host=${REDIS_HOST}
spring.redis.port=${REDIS_PORT}

# JPA
spring.jpa.hibernate.ddl-auto=update

# Email
spring.mail.username=${SPRING_MAIL_USERNAME}
spring.mail.password=${SPRING_MAIL_PASSWORD}

# JWT
app.jwt-secret=${APP_JWT_SECRET}
app.jwt-expiration=${APP_JWT_EXPIRATION}
```

### Security Features
- **Argon2 Hashing:** Salt 16B, Hash 32B, Iterations 1, Memory 65336KB, Parallelism 3
- **ID Generation:** Format `PREFIX.YYYYMMDDHHMMSS.XXXXXXXX` (A-Z, 0-9, 8 chars)

---

## 🧪 Testing

### Run Tests
```bash
mvn test
```

### Coverage
- Service, repository, controller, exception, validation

---

## 🚀 Deployment

### Production Tips
- Use secure env vars
- Enable HTTPS & CORS
- Structured logging
- Health checks
- Rate limiting

### Docker
```dockerfile
FROM openjdk:25-jdk-slim
COPY target/cauth-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

## 🔒 Security Best Practices

- Argon2 password hashing
- Input validation
- SQL injection prevention
- Multi-tenant isolation
- Secure ID generation
- JWT tokens for stateless auth
- Rate limiting
- HTTPS in production
- CORS policies
- Request logging
- Security audits

---

## 🤝 Contributing

1. Fork the repo
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit (`git commit -m 'Add amazing feature'`)
4. Push (`git push origin feature/amazing-feature`)
5. Open a Pull Request

---

## 📄 License

MIT License – see LICENSE file

---

## 🆘 Support

- Create an issue
- Check docs & API examples
- Review test cases

---

## 🗺️ Roadmap

- [ ] JWT token-based authentication
- [ ] OAuth2 integration
- [ ] Email service integration
- [ ] Rate limiting middleware
- [ ] API docs (Swagger)
- [ ] Audit logging
- [ ] User session management
- [ ] Two-factor authentication
- [ ] Password complexity policies
- [ ] Account lockout mechanisms

---

**Cross Auth** – Secure, scalable, multi-tenant authentication made simple. 🚀
