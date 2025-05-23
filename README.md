# Query Builder

Query Builder is a Spring Boot-based service that dynamically connects to **MySQL** or **PostgreSQL** databases. It provides RESTful APIs to retrieve table metadata, generate SQL queries, and access table data with pagination. The service simplifies backend data analysis and integration tasks without requiring manual SQL scripting.

---

## Prerequisites

Before you begin, ensure you have the following installed:

- Java 11
- Maven 3.2
- Git
- MySQL 8.x
- PostgreSQL 14+
- Spring Tool Suite (STS) 
- Postman

---

## Setup Instructions

### 1. Clone the Repository

Clone the project and open it in Spring Tool Suite or your preferred IDE:

```bash
git clone https://github.com/ramprasadps-tts/tm-query-builder-service
```
## MySQL Setup

To run the application with MySQL, follow these steps:

### 1. Start MySQL

Ensure MySQL server is running locally.

### 2. Create Database and Table

You can create the required database and table by executing the provided DDL script.

**DDL Script Location:**

[tm-query-builder-service-ddl.sql](https://github.com/ramprasadps-tts/tm-query-builder-service/blob/postgres-integration/tm-query-builder-service-ddl.sql)

### 3. Execute the Script

Run the script using the MySQL command-line tool:

```bash
mysql -u root -p < tm-query-builder-service-ddl.sql
```
