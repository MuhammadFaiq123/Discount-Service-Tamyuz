# Discount-Service-Tamyuz

Discount Service Tamyuz is a Spring Boot Application to manage **products, users, and orders**, including **dynamic discount computation** based on user type and order amount. The application uses JWT-based authentication, role-based authorization, and exposes Actuator endpoints for monitoring.

Note: Postman collection is given in the root directory.
---

## Table of Contents

- [Features](#features)  
- [Technologies](#technologies)  
- [Prerequisites](#prerequisites)  
- [Setup](#setup)  
- [Swagger](#swagger)
- [Service Flow](#service-flow)

---

## Features

- **JWT-based Authentication**  
- **Role-based Authorization** (`ADMIN`, `USER`, `PREMIUM_USER`)  
- **Order Management** with dynamic discounts:
  - Premium user discount  
  - High-value order discount  
- **Product Catalog** with CRUD operations  
- **Redis caching** for better performance  
- **Liquibase-based DB migrations**  
- **Actuator endpoints** for health, info, and metrics  

---

## Technologies

- Java 21  
- Spring Boot 3.2  
- Spring Security + JWT  
- Spring Data JPA + PostgreSQL  
- Redis (caching)  
- Docker & Docker Compose  
- Liquibase  
- Swagger/OpenAPI 3  

---

## Prerequisites

- Java 21+  
- Maven 3.8+  
- Docker & Docker Compose  
- PostgreSQL 16+  
- Redis 7+  

---

## Setup

### Database & Redis

#### PostgreSQL

Run locally below commands or via Docker:

docker run -d \
  --name my_postgres \
  -e POSTGRES_PASSWORD=mysecretpassword \
  -p 5432:5432 \
  postgres:16

docker run -d \
  --name my_redis \
  -e REDIS_PASSWORD=P@ssw0rd \
  -p 6379:6379 \
  redis:7 redis-server --requirepass P@ssw0rd

mvn clean package

java -jar target/discount-service-tamyuz-0.0.1-SNAPSHOT.jar --spring.profiles.active=docker


## Swagger

after the service is up, access the swagger at below to check all the endpoints:
http://localhost:8888/swagger-ui/index.html


## Service Flow

This section explains how a user interacts with the **Discount Service Tamyuz** application, step by step, from login to accessing secured endpoints.

---

## 1. User Login & JWT Token Generation

1. **Login Endpoint**
   - URL: `POST /auth/login`
   - Request Body:
     ```json
     {
       "email": "user@example.com",
       "password": "password123"
     }
     ```

2. **Authentication Process**
   - The application verifies the email and password against the database.
   - On successful authentication, a **JWT token** is generated containing:
     - User email
     - User role(s) (`USER`, `PREMIUM_USER`, or `ADMIN`)
     - Token expiration time (1 hour)

3. **Login Response**
   - JSON response:
     ```json
     {
       "accessToken": "<JWT_TOKEN>"
     }
     ```

4. **Token Usage**
   - Include this token in the **Authorization header** for all secured endpoints:
     ```
     Authorization: Bearer <JWT_TOKEN>
     ```

---

## 2. Accessing Products

- **List Products (Public)**
  - Endpoint: `GET /api/products`
  - No token required.
  - Returns paginated list of products.

- **Get Product by ID (Public)**
  - Endpoint: `GET /api/products/{id}`
  - No token required.
  - Returns detailed product information.

- **Admin Operations (Requires ADMIN Role)**
  - Create Product: `POST /api/products`
  - Update Product: `PUT /api/products/{id}`
  - Delete Product: `DELETE /api/products/{id}`
  - Include JWT token with `ADMIN` role in Authorization header.

---

## 3. Placing Orders

1. **Place an Order (USER or PREMIUM_USER)**
   - Endpoint: `POST /api/orders`
   - Include JWT token in Authorization header.
   - Request Body Example:
     ```json
     {
       "items": [
         { "productId": 1, "quantity": 2 },
         { "productId": 3, "quantity": 1 }
       ]
     }
     ```
   - Server checks:
     - User exists
     - Product stock availability
   - Discounts applied dynamically:
     - **Premium user discount** (if user is PREMIUM_USER)
     - **High-value order discount** (if order exceeds threshold)
   - Order saved in DB with updated totals and discount applied.

2. **View My Orders**
   - Endpoint: `GET /api/orders?page=0&size=10`
   - Returns paginated orders placed by the authenticated user.
   - Requires JWT token in Authorization header.

---

## 4. Accessing Actuator Endpoints (Admin Only)

- Endpoints: `/actuator/health`, `/actuator/info`, `/actuator/metrics`, `/actuator/complete`
- Only accessible by users with `ADMIN` role.
- Include JWT token in Authorization header.
- Provides application health, metadata, and metrics.

---

## 5. Redis Caching

- Redis is used for **caching frequently accessed data**, like product lists.
- The user does not interact with Redis directly; caching is transparent.

---

## 6. PostgreSQL Database

- Users, products, orders, and order items are stored in PostgreSQL.
- Database migrations are handled by Liquibase on application startup.
- Users only interact through application endpoints; DB operations are automatic.

---

## Summary User Flow

```text
User -> POST /auth/login -> Receives JWT Token
User -> Use JWT in Authorization Header
User -> GET /api/products -> List or fetch product info
User -> POST /api/orders -> Place orders with discounts
Admin -> Access /api/products (create/update/delete)
Admin -> Access /actuator/* -> Health, info, metrics
