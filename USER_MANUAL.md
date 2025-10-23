# Cross Auth - API User Manual

## Table of Contents
1. [Overview](#overview)
2. [Base URL & Authentication](#base-url--authentication)
3. [API Response Format](#api-response-format)
4. [Data Transfer Objects (DTOs)](#data-transfer-objects-dtos)
5. [API Endpoints](#api-endpoints)
6. [Error Handling](#error-handling)
7. [Examples](#examples)

---

## Overview

Cross Auth is a secure, multi-tenant authentication system that provides:
- Organization-level authentication and management
- Application registration and management
- End-user authentication and profile management
- Email verification workflows
- Password management (change/reset)
- JWT-based token generation

---

## Base URL & Authentication

**Base URL:** `https://cauth-api.onrender.com` (default)

### Authentication Methods
- **Organization-level:** Cookie-based authentication (authToken)
- **Application-level:** JWT tokens with app-specific secrets
- **End-user:** JWT tokens and auth codes

---

## API Response Format

All API endpoints return a standardized JSON response:

```json
{
  "success": true,
  "message": "Operation completed successfully",
  "timestamp": "2025-10-23T17:28:52.123456",
  "data": {}
}
```

### Response Fields
| Field | Type | Description |
|-------|------|-------------|
| `success` | boolean | Indicates if the request was successful |
| `message` | string | Human-readable message about the operation |
| `timestamp` | ISO 8601 DateTime | Server timestamp of the response |
| `data` | object/array | Response payload (content varies by endpoint) |

---

## Data Transfer Objects (DTOs)

### User DTOs

#### CreateUserRequestDto
Create a new user in the system.

```json
{
  "email": "user@example.com",
  "appId": "your-app-id",
  "password": "securePassword123",
  "phoneNumber": "+1234567890"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `email` | string | ✓ | Valid email, max 255 chars |
| `appId` | string | ✓ | Max 50 chars |
| `password` | string | ✓ | 8-128 characters |
| `phoneNumber` | string | ✗ | Max 20 chars |

---

#### UserLoginRequestDto
Authenticate a user with credentials.

```json
{
  "email": "user@example.com",
  "appId": "your-app-id",
  "password": "securePassword123"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `email` | string | ✓ | Valid email, max 255 chars |
| `appId` | string | ✓ | Max 50 chars |
| `password` | string | ✓ | Non-empty |

---

#### UserResponseDto
Response containing user information.

```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "authId": "auth_abc123",
  "email": "user@example.com",
  "appId": "your-app-id",
  "isEmailVerified": false,
  "phoneNumber": "+1234567890",
  "createdAt": "2025-10-23T10:00:00",
  "updatedAt": "2025-10-23T15:30:00",
  "role": "USER"
}
```

| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique user identifier |
| `authId` | string | Authentication ID for token generation |
| `email` | string | User's email address |
| `appId` | string | Associated application ID |
| `isEmailVerified` | boolean | Email verification status |
| `phoneNumber` | string | User's phone number |
| `createdAt` | DateTime | Account creation timestamp |
| `updatedAt` | DateTime | Last update timestamp |
| `role` | string | User role (e.g., "USER", "ADMIN") |

---

#### UpdateUserRequestDto
Update user profile information.

```json
{
  "email": "newemail@example.com",
  "phoneNumber": "+9876543210",
  "password": "newPassword123"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `email` | string | ✗ | Valid email, max 255 chars |
| `phoneNumber` | string | ✗ | Max 20 chars |
| `password` | string | ✗ | 8-128 characters |

---

#### ChangePasswordRequestDto
Change user password with current password verification.

```json
{
  "currentPassword": "oldPassword123",
  "newPassword": "newPassword456"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `currentPassword` | string | ✓ | Current password for verification |
| `newPassword` | string | ✓ | 8-128 characters |

---

#### EmailVerificationDto
Verify user email with token.

```json
{
  "authId": "auth_abc123",
  "verificationToken": "email_verification_token_xyz"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `authId` | string | ✓ | Max 50 chars |
| `verificationToken` | string | ✓ | Max 255 chars |

---

### Application DTOs

#### ApplicationRequestDto
Register a new application.

```json
{
  "appName": "My Web App",
  "ownerEmail": "owner@example.com",
  "appSecret": "your-app-secret-key",
  "allowedCallbackUrls": [
    "https://app.example.com/callback",
    "https://app.example.com/auth/callback"
  ]
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `appName` | string | ✓ | Non-empty |
| `ownerEmail` | string | ✓ | Valid email |
| `appSecret` | string | ✓ | Non-empty |
| `allowedCallbackUrls` | array | ✗ | List of valid URLs |

---

#### ApplicationResponseDto
Application information in responses.

```json
{
  "appName": "My Web App",
  "appId": "app_xyz789",
  "ownerEmail": "owner@example.com",
  "dateOfJoining": "2025-10-20T08:00:00",
  "allowedCallbackUrls": [
    "https://app.example.com/callback",
    "https://app.example.com/auth/callback"
  ]
}
```

| Field | Type | Description |
|-------|------|-------------|
| `appName` | string | Application name |
| `appId` | string | Unique application identifier |
| `ownerEmail` | string | Owner's email address |
| `dateOfJoining` | DateTime | Application registration timestamp |
| `allowedCallbackUrls` | array | Registered callback URLs |

---

### Organization DTOs

#### CreateOrgDto
Register a new organization.

```json
{
  "name": "Acme Corporation",
  "email": "admin@acme.com",
  "password": "secureOrgPassword123"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `name` | string | ✓ | Organization name |
| `email` | string | ✓ | Valid email |
| `password` | string | ✓ | Non-empty |

---

#### LoginOrgDto
Authenticate organization.

```json
{
  "email": "admin@acme.com",
  "password": "secureOrgPassword123"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `email` | string | ✓ | Valid email |
| `password` | string | ✓ | Non-empty |

---

#### OrgPublicDto
Organization public information in responses.

```json
{
  "name": "Acme Corporation",
  "email": "admin@acme.com",
  "isActive": true
}
```

| Field | Type | Description |
|-------|------|-------------|
| `name` | string | Organization name |
| `email` | string | Organization email |
| `isActive` | boolean | Organization active status |

---

### Client Authentication Service DTOs

#### TokenRequestDto
Request authentication token from auth code.

```json
{
  "authCode": "auth_code_12ch",
  "appId": "your-app-id",
  "appSecret": "your-app-secret"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `authCode` | string | ✓ | Exactly 12 characters |
| `appId` | string | ✓ | Non-empty |
| `appSecret` | string | ✓ | Non-empty |

---

#### UserIdentificationDto
Identify user by email and app.

```json
{
  "email": "user@example.com",
  "appId": "your-app-id"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `email` | string | ✓ | Valid email |
| `appId` | string | ✓ | Non-empty |

---

#### ResetPasswordRequestDto
Reset user password with security code.

```json
{
  "authId": "auth_abc123",
  "securityCode": "123456",
  "newPassword": "newPassword789"
}
```

| Field | Type | Required | Constraints |
|-------|------|----------|-------------|
| `authId` | string | ✓ | Non-empty |
| `securityCode` | string | ✓ | Exactly 6 characters |
| `newPassword` | string | ✓ | Min 8 characters |

---

## API Endpoints

### Organization Authentication

#### POST `/api/v1/public/auth/register`
Register a new organization.

**Request Body:** `CreateOrgDto`

**Response:** `ApiResponse<OrgPublicDto>`

**Status Code:** 200 OK

**Cookie:** Sets `authToken` cookie (12-hour expiration)

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/public/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Acme Corp",
    "email": "admin@acme.com",
    "password": "SecurePass123"
  }'
```

---

#### POST `/api/v1/public/auth/login`
Authenticate organization.

**Request Body:** `LoginOrgDto`

**Response:** `ApiResponse<OrgPublicDto>`

**Status Code:** 200 OK

**Cookie:** Sets `authToken` cookie (12-hour expiration)

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/public/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@acme.com",
    "password": "SecurePass123"
  }'
```

---

### User Management

#### GET `/api/v1/users`
Retrieve all users (organization scope).

**Response:** `ApiResponse<List<UserResponseDto>>`

**Status Code:** 200 OK

**Authentication:** Required (authToken)

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/users \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### POST `/api/v1/users`
Create a new user.

**Request Body:** `CreateUserRequestDto`

**Response:** `ApiResponse<UserResponseDto>`

**Status Code:** 201 CREATED

**Authentication:** Required

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/users \
  -H "Content-Type: application/json" \
  -H "Cookie: authToken=YOUR_TOKEN" \
  -d '{
    "email": "newuser@example.com",
    "appId": "app_123",
    "password": "UserPass123"
  }'
```

---

#### GET `/api/v1/users/{id}`
Retrieve user by UUID.

**Path Parameters:**
- `id` (UUID): User ID

**Response:** `ApiResponse<UserResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/users/123e4567-e89b-12d3-a456-426614174000 \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### GET `/api/v1/users/auth/{authId}`
Retrieve user by authentication ID.

**Path Parameters:**
- `authId` (string): Authentication ID

**Response:** `ApiResponse<UserResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/users/auth/auth_abc123 \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### GET `/api/v1/users/email`
Retrieve user by email and application ID.

**Query Parameters:**
- `email` (string): User email
- `appId` (string): Application ID

**Response:** `ApiResponse<UserResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/users/email?email=user@example.com&appId=app_123" \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### GET `/api/v1/users/app/{appId}`
Retrieve all users for an application.

**Path Parameters:**
- `appId` (string): Application ID

**Response:** `ApiResponse<List<UserResponseDto>>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/users/app/app_123 \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### GET `/api/v1/users/exists`
Check if user exists by email and application.

**Query Parameters:**
- `email` (string): User email
- `appId` (string): Application ID

**Response:** `ApiResponse<Boolean>`

**Status Code:** 200 OK

**Example:**
```bash
curl -X GET "http://localhost:8080/api/v1/users/exists?email=user@example.com&appId=app_123" \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### PUT `/api/v1/users/auth/{authId}`
Update user information.

**Path Parameters:**
- `authId` (string): Authentication ID

**Request Body:** `UpdateUserRequestDto`

**Response:** `ApiResponse<UserResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X PUT http://localhost:8080/api/v1/users/auth/auth_abc123 \
  -H "Content-Type: application/json" \
  -H "Cookie: authToken=YOUR_TOKEN" \
  -d '{
    "email": "newemail@example.com",
    "phoneNumber": "+1987654321"
  }'
```

---

#### DELETE `/api/v1/users/auth/{authId}`
Delete a user.

**Path Parameters:**
- `authId` (string): Authentication ID

**Response:** `ApiResponse<Void>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/v1/users/auth/auth_abc123 \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### POST `/api/v1/users/authenticate`
Authenticate a user with credentials.

**Request Body:** `UserLoginRequestDto`

**Response:** `ApiResponse<UserResponseDto>`

**Status Code:** 200 OK

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/users/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "appId": "app_123",
    "password": "UserPass123"
  }'
```

---

#### PATCH `/api/v1/users/auth/{authId}/verify-email`
Verify user email.

**Path Parameters:**
- `authId` (string): Authentication ID

**Response:** `ApiResponse<UserResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X PATCH http://localhost:8080/api/v1/users/auth/auth_abc123/verify-email \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

### Application Management

#### GET `/api/v1/apps`
Retrieve all applications.

**Response:** `ApiResponse<List<ApplicationResponseDto>>`

**Status Code:** 200 OK

**Authentication:** Required (authToken)

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/apps \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### POST `/api/v1/apps`
Register a new application.

**Request Body:** `ApplicationRequestDto`

**Response:** `ApiResponse<ApplicationResponseDto>`

**Status Code:** 201 CREATED

**Authentication:** Required

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/apps \
  -H "Content-Type: application/json" \
  -H "Cookie: authToken=YOUR_TOKEN" \
  -d '{
    "appName": "My Web App",
    "ownerEmail": "owner@example.com",
    "appSecret": "secret123",
    "allowedCallbackUrls": ["https://myapp.com/callback"]
  }'
```

---

#### GET `/api/v1/apps/{appId}`
Retrieve application by ID.

**Path Parameters:**
- `appId` (string): Application ID

**Response:** `ApiResponse<ApplicationResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/apps/app_xyz789 \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### GET `/api/v1/apps/token-sign-key/{appId}`
Retrieve JWT signing secret for application.

**Path Parameters:**
- `appId` (string): Application ID

**Response:** `ApiResponse<String>` (contains JWT secret)

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X GET http://localhost:8080/api/v1/apps/token-sign-key/app_xyz789 \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

#### PATCH `/api/v1/apps/{appId}`
Update application settings.

**Path Parameters:**
- `appId` (string): Application ID

**Request Body:** JSON object with fields to update

**Response:** `ApiResponse<ApplicationResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X PATCH http://localhost:8080/api/v1/apps/app_xyz789 \
  -H "Content-Type: application/json" \
  -H "Cookie: authToken=YOUR_TOKEN" \
  -d '{
    "appName": "Updated App Name",
    "allowedCallbackUrls": ["https://newapp.com/callback"]
  }'
```

---

#### DELETE `/api/v1/apps/{appId}`
Delete an application.

**Path Parameters:**
- `appId` (string): Application ID

**Response:** `ApiResponse<ApplicationResponseDto>`

**Status Code:** 200 OK

**Authentication:** Required

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/v1/apps/app_xyz789 \
  -H "Cookie: authToken=YOUR_TOKEN"
```

---

### Client Authentication Service

These endpoints are public and used by client applications to handle end-user authentication.

#### POST `/api/v1/public/services/cauth1/register`
Register a new end-user.

**Request Body:** `CreateUserRequestDto`

**Response:** `ApiResponse<String>` (auth code)

**Status Code:** 200 OK

**Authentication:** Not required

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "newuser@example.com",
    "appId": "app_123",
    "password": "UserPass123"
  }'
```

---

#### POST `/api/v1/public/services/cauth1/login`
Authenticate end-user.

**Request Body:** `UserLoginRequestDto`

**Response:** `ApiResponse<String>` (auth code)

**Status Code:** 200 OK

**Authentication:** Not required

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "appId": "app_123",
    "password": "UserPass123"
  }'
```

---

#### POST `/api/v1/public/services/cauth1/a`
Identify user by email and application ID.

**Request Body:** `UserIdentificationDto`

**Response:** `ApiResponse<String>` (auth ID)

**Status Code:** 200 OK

**Authentication:** Not required

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/a \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "appId": "app_123"
  }'
```

---

#### POST `/api/v1/public/services/cauth1/reset-password`
Reset user password with security code.

**Request Body:** `ResetPasswordRequestDto`

**Response:** `ApiResponse<String>` (success message)

**Status Code:** 200 OK

**Authentication:** Not required

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/reset-password \
  -H "Content-Type: application/json" \
  -d '{
    "authId": "auth_abc123",
    "securityCode": "123456",
    "newPassword": "NewPass789"
  }'
```

---

#### POST `/api/v1/public/services/cauth1/token`
Exchange auth code for JWT token.

**Request Body:** `TokenRequestDto`

**Response:** `ApiResponse<String>` (JWT token)

**Status Code:** 200 OK

**Authentication:** Not required

**Example:**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/token \
  -H "Content-Type: application/json" \
  -d '{
    "authCode": "auth_code_12ch",
    "appId": "app_123",
    "appSecret": "your-app-secret"
  }'
```

---

## Error Handling

### Error Response Format

```json
{
  "success": false,
  "message": "Error message describing what went wrong",
  "timestamp": "2025-10-23T17:28:52.123456",
  "data": null
}
```

### Common HTTP Status Codes

| Status | Meaning | Use Case |
|--------|---------|----------|
| 200 | OK | Successful GET, PUT, PATCH, POST |
| 201 | Created | Successful resource creation |
| 400 | Bad Request | Invalid input/validation error |
| 401 | Unauthorized | Missing or invalid authentication |
| 403 | Forbidden | Insufficient permissions |
| 404 | Not Found | Resource not found |
| 409 | Conflict | Resource already exists |
| 500 | Internal Server Error | Server-side error |

### Common Error Messages

| Error | Possible Cause | Solution |
|-------|----------------|----------|
| "Email is required" | Missing email field | Include valid email in request |
| "Email must be valid" | Invalid email format | Use valid email format (user@domain.com) |
| "Password must be between 8 and 128 characters" | Password too short/long | Use 8-128 character password |
| "User not found" | User doesn't exist | Check email and appId |
| "Invalid credentials" | Wrong password | Verify credentials |
| "Application not found" | Invalid appId | Use valid registered appId |
| "User already exists" | Email already registered for app | Use different email or check existing user |

---

## Examples

### Complete User Registration Flow

**Step 1: Register organization**
```bash
curl -X POST http://localhost:8080/api/v1/public/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "My Company",
    "email": "admin@mycompany.com",
    "password": "AdminPass123"
  }'
```

**Step 2: Register application (as organization)**
```bash
curl -X POST http://localhost:8080/api/v1/apps \
  -H "Content-Type: application/json" \
  -H "Cookie: authToken=RECEIVED_TOKEN" \
  -d '{
    "appName": "Mobile App",
    "ownerEmail": "admin@mycompany.com",
    "appSecret": "mobile-app-secret",
    "allowedCallbackUrls": ["https://myapp.com/callback"]
  }'
```

**Step 3: Register end-user (public endpoint)**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "enduser@example.com",
    "appId": "app_received_from_step2",
    "password": "UserPass123"
  }'
```

**Step 4: Exchange auth code for token**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/token \
  -H "Content-Type: application/json" \
  -d '{
    "authCode": "auth_code_received_from_step3",
    "appId": "app_received_from_step2",
    "appSecret": "mobile-app-secret"
  }'
```

---

### Complete User Login Flow

**Step 1: User login (public endpoint)**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "enduser@example.com",
    "appId": "app_123",
    "password": "UserPass123"
  }'
```

**Step 2: Exchange auth code for JWT token**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/token \
  -H "Content-Type: application/json" \
  -d '{
    "authCode": "auth_code_from_step1",
    "appId": "app_123",
    "appSecret": "app_secret"
  }'
```

---

### Password Reset Flow

**Step 1: Identify user**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/a \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "appId": "app_123"
  }'
```

**Step 2: Reset password with security code**
```bash
curl -X POST http://localhost:8080/api/v1/public/services/cauth1/reset-password \
  -H "Content-Type: application/json" \
  -d '{
    "authId": "auth_id_from_step1",
    "securityCode": "123456",
    "newPassword": "NewPassword789"
  }'
```

---

## Security Notes

1. **Always use HTTPS** in production
2. **Store tokens securely** - Use secure cookies (HttpOnly flag)
3. **Validate input** - All DTOs include validation constraints
4. **Protect secrets** - Never expose `appSecret` or authentication tokens in logs
5. **Password requirements** - Passwords must be 8-128 characters
6. **Auth codes** - Single-use, short-lived (12 characters)
7. **Security codes** - 6-digit codes for password reset
8. **Email verification** - Implement email verification workflow in production

---

## Support & Documentation

For more information, refer to:
- GitHub repository documentation
- API source code and javadoc comments
- Server logs for detailed error information

**Last Updated:** October 23, 2025

