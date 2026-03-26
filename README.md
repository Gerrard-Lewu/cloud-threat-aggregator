# Cloud-Native Threat Intelligence Aggregator API

A containerized Spring Boot microservice deployed on AWS Fargate that automatically aggregates, standardizes, and stores malicious IP data from external threat intelligence feeds (AbuseIPDB).

This API is designed to reduce external API latency in SIEM (Security Information and Event Management) environments by maintaining a localized, high-speed, and secure cloud database of known threat actors.

## ️ Cloud Architecture

![](C:\Users\DellUser\Documents\Projects\fraud-architecture-diagram.png)([PLACEHOLDER: Insert link to your draw.io image here])

### Infrastructure Breakdown
This project demonstrates cloud networking, container orchestration, and security best practices:

* **Compute (Amazon ECS with Fargate):** The Spring Boot application is containerized via Docker and runs on serverless Fargate compute, eliminating the need to manage underlying EC2 instances while ensuring high availability.
* **Registry (Amazon ECR):** Secure, private storage for the compiled application images.
* **Database (Amazon RDS - PostgreSQL):** A fully managed relational database deployed within a **private subnet**.
* **Security & Networking (VPC & Security Groups):** * The database is isolated from the public internet (`Public Access: No`).
    * Employs the principle of least privilege: the RDS Security Group only accepts inbound TCP connections on port 5432 strictly from the ECS Fargate Security Group ID.
    * Configuration management is handled via 12-Factor App principles using AWS Environment Variables (no hardcoded credentials).

##  Technology Stack
* **Backend:** Java 21, Spring Boot 3.x, Spring Data JPA, Hibernate
* **Database:** PostgreSQL
* **Containerization:** Docker
* **Cloud Provider:** AWS (ECS, Fargate, ECR, RDS, IAM, CloudWatch)
* **External Integration:** AbuseIPDB API

##  API Endpoints

**Base URL (Click to test):** [http://13.246.188.193:8080](http://13.246.188.193:8080)
**Live Demo (Click to test):** [http://13.246.188.193:8080/api/v1/threats/check?ip=192.168.1.99](http://13.246.188.193:8080/api/v1/threats/check?ip=192.168.1.99)

### 1. Check IP Threat Status
Evaluates an IP address against the aggregated localized database.

* **URL:** `/api/v1/threats/check`
* **Method:** `GET`
* **Parameters:** `?ip=[ip_address]`

**Success Response (Threat Found):**
```json
{
  "id": 15,
  "ipAddress": "45.79.115.134",
  "threatLevel": "HIGH (Score: 100)",
  "source": "AbuseIPDB"
}
```

**Success Response (IP Safe):**
```json
{
  "status": "SAFE", 
  "message": "IP not found in threat database"
}
```

### 2. Add Test Data
A temporary utility endpoint to verify secure database connectivity from the public API into the private RDS subnet.

* **URL:** `/api/v1/threats/add-test-data`
* **Method:** `POST`

##  Core Application Logic

* **Automated Ingestion:** A Spring `@Scheduled` worker wakes up every hour to ping external threat intelligence feeds via modern `RestClient`.
* **Deduplication:** The ingestion service checks the PostgreSQL database before saving to ensure rapid execution and prevent data bloat.
* **JSON Serialization:** Utilizes Java Records for lightweight, immutable data transfer objects when parsing external API responses.

##  Future Enhancements
To scale this architecture for high-volume production traffic, the following AWS services would be introduced:
1.  **Application Load Balancer (ALB):** To provide a static DNS endpoint and distribute traffic across multiple Fargate tasks.
2.  **Amazon Route 53 & ACM:** To attach a custom domain name and secure the API with HTTPS/TLS encryption.
3.  **Amazon ElastiCache (Redis):** To cache frequent IP checks, reducing the read-load on the RDS instance.