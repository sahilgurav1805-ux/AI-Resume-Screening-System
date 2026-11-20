# AI Resume Screening System

![Java](https://img.shields.io/badge/Java-23-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-Backend-6DB33F?logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-Database-4479A1?logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-000000?logo=jsonwebtokens&logoColor=white)
![Ollama](https://img.shields.io/badge/AI-Ollama_%2B_Gemma_3-555555)

A Spring Boot backend that analyzes PDF resumes against job descriptions, identifies matched and missing skills, evaluates resume formatting, and generates AI-based feedback using a locally hosted LLM (Ollama with Gemma 3).

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Application Workflow](#application-workflow)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Security](#security)
- [API Reference](#api-reference)
- [Future Enhancements](#future-enhancements)
- [Author](#author)

## Overview

The AI Resume Screening System simplifies resume evaluation for students and recruiters. It combines rule-based skill matching with AI-powered analysis to give candidates actionable feedback and recruiters a consistent view of how a resume fits a role.

## Features

| Feature | Description |
|---|---|
| PDF resume upload | Accepts resumes in PDF format. |
| Skill extraction | Extracts technical skills from uploaded resumes. |
| Skill matching | Compares resume skills with job requirements. |
| Missing skills detection | Identifies required skills absent from the resume. |
| AI-powered analysis | Generates detailed feedback using Ollama and Gemma 3. |
| Formatting analysis | Analyzes PDF formatting and suggests improvements. |
| JWT authentication | Secures the API with Spring Security and JWT. |
| Role-based authorization | Separate Student and Recruiter roles. |
| Analysis history | Stores and retrieves previous resume analyses. |

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 23 | Backend programming |
| Spring Boot | Application framework |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| Spring Data JPA | Data access and persistence |
| MySQL | Relational database |
| Apache PDFBox | PDF text extraction |
| Ollama | Local LLM runtime |
| Gemma 3 (4B) | AI resume analysis |
| Maven | Build and dependency management |
| Postman | API testing |

## System Architecture

The application follows a layered backend architecture.

````text
             Client / Postman
                    |
                    v
             Spring Boot API
                    |
          +---------+---------+
          |         |         |
          v         v         v
      Security   Controller  AI Module
          |         |         |
          v         v         v
         JWT     Services   Ollama
                    |
          +---------+---------+
          |                   |
          v                   v
      Repository         PDF Processing
          |
          v
        MySQL
````

## Application Workflow

1. The user registers and logs in.
2. The user uploads a resume in PDF format.
3. Apache PDFBox extracts text from the resume.
4. The system identifies technical skills.
5. Extracted skills are compared against the required job skills.
6. The system identifies matched and missing skills.
7. Resume formatting is analyzed.
8. Ollama generates AI-based resume feedback.
9. A consolidated analysis report is returned to the user.

## Project Structure

````text
resume-checker/
+-- src/
|   +-- main/
|       +-- java/com/sahil/resume_checker/
|       |   +-- config/
|       |   +-- controller/
|       |   +-- dto/
|       |   +-- entity/
|       |   +-- repository/
|       |   +-- security/
|       |   +-- service/
|       +-- resources/
|           +-- application.properties
+-- pom.xml
+-- README.md
````

## Getting Started

### Prerequisites

- Java 23 or a compatible JDK
- Maven
- MySQL
- [Ollama](https://ollama.com)
- Postman (optional, for API testing)

### Installation

1. Clone the repository and open the project directory:

   ````bash
   git clone https://github.com/sahilgurav1805-ux/AI-Resume-Screening-System.git
   cd AI-Resume-Screening-System
   ````

2. Create the MySQL database:

   ````sql
   CREATE DATABASE resume_checker;
   ````

3. Configure `application.properties` with your local database details:

   ````properties
   spring.datasource.url=jdbc:mysql://localhost:3306/resume_checker
   spring.datasource.username=YOUR_USERNAME
   spring.datasource.password=YOUR_PASSWORD
   ````

   Set your JWT secret and any other required properties in the same file.

   > **Security note:** Never commit real passwords, JWT secrets or other credentials to version control.

4. Install Ollama, pull the model and make sure the service is running before requesting AI analysis:

   ````bash
   ollama pull gemma3:4b
   ````

5. Run the application:

   ````bash
   mvn spring-boot:run
   ````

   To build the project instead:

   ````bash
   mvn clean install
   ````

## Security

- Spring Security integration
- JWT-based authentication
- BCrypt password hashing
- Stateless session management
- Role-based authorization (Student and Recruiter)
- Protected API endpoints

## API Reference

Endpoints can be tested with Postman.

**Authentication**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Log in and receive a JWT |

**Resume analysis**

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/ai/analyze` | Analyze a resume |
| POST | `/api/ai/analyze-complete` | Run the complete analysis |
| GET | `/api/ai/history` | Retrieve previous analyses |

Protected endpoints require the JWT in the request header:

````http
Authorization: Bearer YOUR_JWT_TOKEN
````

## Future Enhancements

- Recruiter dashboard for screening multiple resumes
- Bulk resume screening
- Candidate ranking and comparison
- Improved AI-generated recommendations
- Frontend integration
- Downloadable resume analysis reports

## Author

**Sahil Gurav**

- GitHub: [sahilgurav1805-ux](https://github.com/sahilgurav1805-ux)
- LinkedIn: [sahilgurav18](https://www.linkedin.com/in/sahilgurav18/)
````
````

Use the copy button at the top right of the block above, then paste it into `README.md` in your repo. Before committing, check that the folder structure and API endpoints match your actual project.
