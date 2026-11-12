# Lab 22: CI/CD Pipeline with Jenkins Agents and Shared Libraries

## Objective

Implement a Jenkins CI/CD pipeline using a dedicated Jenkins agent and reusable Shared Libraries to automate application testing, Docker image creation, security scanning, image publishing, and Kubernetes deployment.

## Repository Structure

```text
lab22-jenkins-shared-libraries/
├── README.md
├── Jenkinsfile
├── k8s/
│   └── deployment.yaml
└── shared-library/
    └── vars/
        ├── BuildApp.groovy
        ├── BuildImage.groovy
        └── DeployOnK8s.groovy
```

## Technologies Used

- Jenkins and Jenkins Agents
- Groovy Shared Libraries
- Git and GitHub
- Maven and Java 17
- Docker
- Trivy
- Kubernetes and kubectl
- Minikube

## Jenkins Agent Configuration

A permanent Jenkins agent named `linux-agent` was configured with:

- Remote root directory: `/home/jenkins-agent/agent`
- Label: `linux-agent`
- Two executors
- WebSocket connection
- Docker, Maven, Trivy, Java, and kubectl installed
- Kubernetes access configured through `KUBECONFIG`

The pipeline is assigned to this agent using:

```groovy
agent {
    label 'linux-agent'
}
```

## Jenkins Shared Library

The following reusable pipeline steps were implemented:

### 1. BuildApp

File: `shared-library/vars/BuildApp.groovy`

- Runs Maven unit tests.
- Packages the application using Maven.
- Verifies the generated JAR artifact.

### 2. BuildImage

File: `shared-library/vars/BuildImage.groovy`

- Builds a Docker image.
- Scans the image using Trivy.
- Pushes the image to Docker Hub using Jenkins credentials.
- Removes the local image after the push or scan attempt.

### 3. DeployOnK8s

File: `shared-library/vars/DeployOnK8s.groovy`

- Updates the Kubernetes deployment manifest with the new image tag.
- Applies the manifest to the `ivolve` namespace.
- Checks the deployment rollout status.
- Displays deployment and pod information.

## Jenkins Global Pipeline Library

Configure the following library under:

**Manage Jenkins → System → Global Pipeline Libraries**

| Setting | Value |
|---|---|
| Name | `jenkins-shared-library` |
| Default version | `publish-lab22` |
| SCM | Git |
| Repository | `https://github.com/ShimaaOmar5/Ivolve_training_cohort15.git` |
| Library path | `CI-CD/lab22-jenkins-shared-libraries/shared-library` |

## Pipeline Configuration

Create a Jenkins Pipeline job with:

- Definition: Pipeline script from SCM
- SCM: Git
- Repository: `https://github.com/ShimaaOmar5/Ivolve_training_cohort15.git`
- Branch: `publish-lab22`
- Script path: `CI-CD/lab22-jenkins-shared-libraries/Jenkinsfile`

Configure a Jenkins credential with ID `dockerhub-credentials` for Docker Hub authentication.

## Pipeline Stages

1. **Checkout DemoApp** – Clones the application source code.
2. **Unit Test** – Runs Maven tests.
3. **Build App** – Packages the Java application.
4. **Build Image** – Builds the Docker image.
5. **Scan Image** – Scans the image using Trivy.
6. **Push Image** – Publishes the image to Docker Hub.
7. **Delete Image Locally** – Removes the local Docker image.
8. **Edit Deployment File** – Updates the Kubernetes image reference.
9. **Deploy to Kubernetes** – Applies the deployment and checks rollout status.

## Kubernetes Deployment

The application is deployed to:

- Namespace: `ivolve`
- Deployment: `demoapp-lab21`
- Replicas: `2`
- Container port: `8080`

Manifest: `k8s/deployment.yaml`

## Execution

1. Configure the Jenkins agent and ensure it is online.
2. Configure the Global Pipeline Library.
3. Configure Docker Hub credentials.
4. Create the Pipeline job using the specified Jenkinsfile path.
5. Run the job and inspect the stage logs in Console Output.

## Notes

- The DemoApp project targets Java 17. The Jenkins agent build environment must use a compatible JDK.
- Trivy requires access to its vulnerability databases. Database download failures can occur because of network connectivity issues.
- The Maven test phase can complete without executing test cases if the application repository does not contain test sources.
- Docker Hub credentials and Jenkins agent secrets must be stored securely in Jenkins or on the agent, never committed to Git.
