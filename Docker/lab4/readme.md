cat > Docker/lab4/README.md <<'EOF'
# Lab 4: Docker Image Optimization and Export/Load

## Objectives
- Build the original Docker image.
- Optimize the image using a multi-stage build.
- Run the application as a non-root user.
- Compare image sizes.
- Export and restore the Docker image.

## Files
- Dockerfile: Original Docker image.
- Dockerfile.optimized: Multi-stage optimized Docker image.
- pom.xml: Maven project configuration.
- src/: Spring Boot application source code.

## Results
- Original image size: 239412821 bytes.
- Optimized image size: 85708393 bytes.
- Image size reduction: 64.20%.
- Application response: Hello from Dockerized Spring Boot!
- Runtime user: spring (UID 100).

## Build the images
```bash
docker build -t image1 .
docker build -f Dockerfile.optimized -t image2 .
