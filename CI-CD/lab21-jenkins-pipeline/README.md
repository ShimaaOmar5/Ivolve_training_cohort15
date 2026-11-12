# Lab 21: Jenkins CI/CD Pipeline for Kubernetes Deployment

## Overview

This lab implements an automated Continuous Integration and Continuous Delivery (CI/CD) pipeline using Jenkins.

The pipeline builds a Spring Boot application, packages it into a Docker image, scans the image for security vulnerabilities using Trivy, pushes it to DockerHub, and deploys it to a Kubernetes cluster running on Minikube.

## Objectives

- Automate application builds using Maven.
- Build and publish Docker images.
- Integrate Trivy for container vulnerability scanning.
- Manage DockerHub credentials securely through Jenkins Credentials.
- Automate Kubernetes deployment updates.
- Verify application rollout and deployment status.
- Configure Jenkins post-build actions.

## Technology Stack

| Technology | Purpose |
|---|---|
| Jenkins | CI/CD automation |
| Java 17 | Application runtime and compilation |
| Maven | Application build |
| Docker | Containerization |
| DockerHub | Container image registry |
| Trivy | Vulnerability scanning |
| Kubernetes | Container orchestration |
| Minikube | Local Kubernetes cluster |
| Git | Source code management |

## Source Code

- Repository: https://github.com/Ibrahim-Adel15/DemoApp
- Application: Spring Boot
- Build tool: Maven

## Repository Structure

```text
lab21-jenkins-pipeline/
├── README.md
├── Jenkinsfile
├── deployment.yaml
├── trivy-scan-summary.md
├── pipeline-architecture.md
└── screenshots/
```

## Jenkins Pipeline

The pipeline consists of eight stages:

### 1. Clone Source Code

Clones the DemoApp GitHub repository into the Jenkins workspace.

### 2. Build Application

Builds the Spring Boot application using:

```bash
mvn clean package
```

Verifies the generated JAR file:

`target/demo-0.0.1-SNAPSHOT.jar`

### 3. Build Docker Image

Builds a Docker image using the application's Dockerfile.

The image tag is generated dynamically using the DockerHub username and Jenkins build number:

`DOCKERHUB_USERNAME/demoapp:BUILD_NUMBER`

### 4. Scan Docker Image

Uses Trivy to scan the Docker image.

- Generates a JSON vulnerability report.
- Displays HIGH and CRITICAL vulnerabilities in the Jenkins console.
- Uses a 20-minute timeout.
- Archives the generated report.

The scan reports vulnerabilities but currently does not block the pipeline based on severity.

See [Trivy Scan Summary](trivy-scan-summary.md) for the recorded results.

### 5. Push Image to DockerHub

Authenticates with DockerHub using a Jenkins credential:

`dockerhub-credentials`

Pushes the image to the DockerHub repository.

### 6. Delete Local Image

Removes the locally built image after it has been successfully pushed to DockerHub.

### 7. Update Image in Deployment File

Generates a Kubernetes Deployment manifest dynamically using the current Docker image tag.

### 8. Deploy to Kubernetes

Applies the deployment manifest and verifies the rollout:

```bash
kubectl apply -f deployment.yaml

kubectl rollout status deployment/demoapp-lab21 \
  -n ivolve --timeout=180s
```

The deployment runs two replicas in the `ivolve` namespace.

## Jenkins Configuration

### Required Jenkins Plugins

- Pipeline
- Credentials Binding
- Git
- Role-based Authorization Strategy (if required by the previous lab)

### Required Jenkins Credentials

| Credential ID | Type | Purpose |
|---|---|---|
| dockerhub-credentials | Username with password | DockerHub authentication |

The password field must contain a DockerHub access token, not a hardcoded password in the pipeline.

### Agent Requirements

The Jenkins agent must have access to:

- Java 17
- Maven
- Docker CLI and Docker daemon
- Trivy
- kubectl
- Minikube Kubernetes cluster configuration

The pipeline uses:

`/var/lib/jenkins/.kube/config`

The Jenkins service account must have permission to access the Kubernetes configuration and cluster.

## Deployment Configuration

| Setting | Value |
|---|---|
| Deployment | demoapp-lab21 |
| Namespace | ivolve |
| Replicas | 2 |
| Container | demoapp |
| Container Port | 8080 |
| Image Pull Policy | Always |
| Kubernetes Cluster | Minikube |

A reusable manifest template is available in [deployment.yaml](deployment.yaml).

## Pipeline Post Actions

| Condition | Action |
|---|---|
| always | Archive Trivy report and deployment manifest |
| success | Display successful deployment message |
| failure | Display pipeline failure message |

## Execution Results

The pipeline completed successfully.

| Item | Result |
|---|---|
| Jenkins Build | #2 |
| Pipeline Status | SUCCESS |
| DockerHub Image | alshaimaamo/demoapp:2 |
| Kubernetes Deployment | demoapp-lab21 |
| Namespace | ivolve |
| Desired Replicas | 2 |
| Rollout | Successful |

The Jenkins console confirmed:

```text
SUCCESS: Application deployed to Kubernetes.
Finished: SUCCESS
```

## Screenshots

Screenshots can be added to the `screenshots/` directory.

Recommended evidence:

| File | Description |
|---|---|
| 01-jenkins-pipeline-success.png | Successful Jenkins pipeline execution |
| 02-jenkins-console-success.png | Final successful pipeline console output |
| 03-trivy-scan-results.png | Trivy vulnerability scan results |
| 04-dockerhub-image.png | Published DockerHub image |
| 05-kubernetes-deployment.png | Kubernetes deployment and pod status |

## Key Concepts Learned

- Jenkins Declarative Pipeline
- CI/CD automation
- Maven application builds
- Docker image creation and publishing
- Jenkins Credentials Management
- Container vulnerability scanning
- Kubernetes Deployment manifests
- Automated Kubernetes rollouts
- Jenkins post-build actions

## Security Considerations

The Trivy scan detected 143 vulnerabilities, including 9 CRITICAL and 40 HIGH findings.

The current pipeline reports these findings without blocking image publication or deployment. The findings should be reviewed and remediated before production use.

Credentials are managed through Jenkins Credentials and are not stored in source code.
