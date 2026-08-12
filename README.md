
# Student Management System - Backend

A Spring Boot REST API for managing student and admin records, built with a complete CI/CD pipeline using Jenkins, Docker, and (planned) Kubernetes deployment.

## Tech Stack

- **Language:** Java 21
- **Framework:** Spring Boot 2.5.7
- **Build Tool:** Maven
- **Database:** PostgreSQL (Hibernate/JPA)
- **CI/CD:** Jenkins
- **Containerization:** Docker
- **Orchestration:** Kubernetes (manifests included)
- **Version Control:** Git / GitHub

## Features

- Add single/multiple student records
- Find student by ID or full name
- List all students
- Update single/multiple student records
- Remove student by ID
- Admin management

## API Endpoints

Base path: `/students`

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/students/add` | Add a new student |
| POST | `/students/addList` | Add multiple students |
| GET | `/students/findbyid/{id}` | Find student by ID |
| GET | `/students/find/{fname},{lname}` | Find student(s) by name |
| GET | `/students/findall` | List all students |
| PUT | `/students/update/add` | Update a student |
| PUT | `/students/update/addList` | Update multiple students |
| DELETE | `/students/remove/{id}` | Delete a student |

## CI/CD Pipeline

This project uses **Jenkins** for continuous integration and delivery:

1. **Checkout** — Pulls the latest code from GitHub on every build
2. **Build & Test** — Runs `mvn clean package`, compiling the code and executing unit tests
3. **Docker Build** — Packages the application into a Docker image
4. **Docker image** Push image to Docker Hub and deploy via Kubernetes

Pipeline steps are defined in the Jenkins job's shell script (see `Jenkinsfile` for the equivalent pipeline-as-code version).

## Running Locally

```bash
mvn clean package
java -jar target/schoolms-0.0.1-SNAPSHOT.war
```

## Running with Docker

Build the image:
```bash
docker build -t studentms-backend .
```

Run PostgreSQL and the app together on a shared network:
```bash
docker network create studentms-net

docker run -d --name postgres-db --network studentms-net \
  -e POSTGRES_DB=schoolmsdb \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  postgres:15

docker run -d -p 8081:8585 --name studentms-final --network studentms-net \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/schoolmsdb \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  studentms-backend:latest
```

Verify it's running:
```bash
curl http://localhost:8081/students/findall
```

## Kubernetes Deployment

Manifests are available in the `k8s/` folder:
- `k8s/deployment.yaml` — Deployment spec (2 replicas)
- `k8s/service.yaml` — LoadBalancer service exposing port 80 → 8080

To deploy (tested locally with Minikube):
```bash
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
kubectl get pods
kubectl get svc
```

## Project Structure

```
studentms-backend/
├── src/                    # Application source code
├── k8s/                    # Kubernetes manifests
│   ├── deployment.yaml
│   └── service.yaml
├── Dockerfile              # Docker build configuration
├── Jenkinsfile             # CI/CD pipeline as code
├── pom.xml                 # Maven configuration
└── README.md
```

## Screenshots

| Description | File |
|---|---|
| Jenkins build success | `jenkins-build-success.png` |
| Jenkins pipeline status | `jenkins-pipeline-status.png` |
| Docker container running | `docker-container-running.png` |
| API responding successfully | `application-api-working.png` |

## Author

Hemalatha S
