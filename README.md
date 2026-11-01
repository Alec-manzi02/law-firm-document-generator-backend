# Law Firm Document Generator Backend

A Spring Boot backend application built for the Web Technology midterm exam at Adventist University of Central Africa (AUCA).

## Project Overview

- **Student Name:** Manzi Alec
- **Student ID:** 26905
- **Project Name:** Law Firm Document Generator
- **Framework & Stack:** Java 17+, Spring Boot 3.3.4, Spring Data JPA, Hibernate, PostgreSQL, Maven, Postman

## Getting Started

### Prerequisites

- JDK 17 or higher
- Apache Maven
- PostgreSQL

### Database Configuration

Create a PostgreSQL database named `law_firm_db`. Set the `DB_PASSWORD` environment variable to the password for your PostgreSQL user before starting the application. The database username defaults to `postgres`; override it with `DB_USERNAME` if needed.

### Run the application

```shell
mvn spring-boot:run
```

### Run tests

```shell
mvn test
```

The Postman collection is available in `law-firm-document-generator-midterm.postman_collection.json`.
