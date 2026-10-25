# Lab 8: Containerized Node.js and MySQL Stack Using Docker Compose

## Overview

This lab demonstrates how to containerize a Node.js web application and a MySQL database using Docker Compose.

The application and database run in separate containers connected through a custom Docker network. Docker volumes provide persistent storage for MySQL data and application logs.

## Objectives

- Containerize the Node.js application using a Dockerfile.
- Run the application and MySQL using Docker Compose.
- Configure environment variables for database connectivity.
- Create a custom bridge network named `ivolve-net`.
- Persist MySQL data and application logs using Docker volumes.
- Configure a MySQL health check and application startup dependency.
- Build, tag, and push the application image to Docker Hub.

## Architecture

```text
                  Host Machine
                       |
                  Port 3000
                       |
                +--------------+
                |  Node.js App |
                |  ivolve-app  |
                +--------------+
                       |
                 ivolve-net
                       |
                +--------------+
                |    MySQL     |
                |  ivolve-db   |
                +--------------+
                       |
                    db_data
                (Persistent Data)

Application Logs:
ivolve-app -> app_logs -> /app/logs
```

## Project Structure

```text
lab8/
├── .dockerignore
├── .gitignore
├── .env                  # Local environment variables; do not commit
├── Dockerfile
├── docker-compose.yml
├── db.js
├── package.json
├── server.js
└── frontend/
    ├── index.html
    └── assets/
        └── ivolve-logo.png
```

## Technologies Used

- Docker
- Docker Compose
- Node.js
- MySQL 8.0
- Linux containers
- Docker volumes and bridge networks
- Docker Hub

## Configuration

Create a `.env` file inside the `lab8` directory with the following variables:

```env
MYSQL_ROOT_PASSWORD=replace_with_a_strong_password
MYSQL_USER=ivolve_user
MYSQL_PASSWORD=replace_with_a_strong_password
```

Replace the example password values with your own. Keep `.env` private and never commit it to Git.

The Compose configuration uses these variables to configure MySQL and the application. The database name is `ivolve`, and the application connects to the database service using the hostname `db`.

## Dockerfile

The Dockerfile builds the Node.js application image.

```dockerfile
FROM node:18-alpine

WORKDIR /app

COPY package.json ./
RUN npm install

COPY . .

EXPOSE 3000

CMD ["node", "server.js"]
```

The `.dockerignore` file excludes files that should not be copied into the image:

```text
.env
.git
node_modules
logs
```

## Docker Compose Configuration

The `docker-compose.yml` file defines two services:

### Application service

- Builds the image from the local Dockerfile.
- Publishes container port `3000` on host port `3000`.
- Receives database connection settings through environment variables.
- Mounts a named volume at `/app/logs`.
- Waits for the database health check to pass before starting.

### MySQL service

- Uses the official `mysql:8.0` image.
- Creates the `ivolve` database and configured database user.
- Persists database files in the `db_data` volume.
- Uses a health check to verify that MySQL is responding.

Both services connect to the custom bridge network `ivolve-net`.

## Run the Application

Run these commands from the `Docker/lab8` directory.

### 1. Build and start the containers

```bash
docker compose up --build -d
```

### 2. Check the service status

```bash
docker compose ps
```

Wait until the database reports a healthy status and the application is running.

### 3. View application logs

```bash
docker compose logs app
```

To follow logs continuously:

```bash
docker compose logs -f app
```

### 4. Access the application

Open the following address in your browser:

http://localhost:3000

The application also exposes these endpoints:

| Endpoint | Purpose |
|---|---|
| `/health` | Checks whether the application is responding |
| `/ready` | Checks application readiness |

Test the endpoints from the terminal:

```bash
curl -i http://localhost:3000/health
curl -i http://localhost:3000/ready
```

A successful response should return HTTP `200`.

## Verify Database Connectivity

Check the application logs:

```bash
docker compose logs app
```

The application should report that it connected to MySQL and found the `ivolve` database.

You can also connect to MySQL interactively:

```bash
docker compose exec db mysql -uivolve_user -p -D ivolve
```

Enter the `MYSQL_PASSWORD` value from your local `.env` file when prompted.

Inside the MySQL prompt, run:

```sql
SHOW DATABASES;
SELECT DATABASE();
SHOW TABLES;
EXIT;
```

The `ivolve` database should exist. Tables will appear only if the application or another initialization process has created them.

## Verify Persistent Storage

### MySQL data volume

```bash
docker volume inspect db_data
```

The named volume `db_data` stores MySQL data at `/var/lib/mysql` inside the database container.

### Application logs volume

```bash
docker volume inspect ivolve_app_logs
```

The named volume `ivolve_app_logs` stores application logs at `/app/logs`.

Check the access log:

```bash
docker compose exec app cat /app/logs/access.log
```

Requests to the application endpoints should appear in the log.

### Verify the custom network

```bash
docker network inspect ivolve-net
```

Both `ivolve-app` and `ivolve-db` should be attached to this network.

## Build and Push the Image to Docker Hub

The local application image is tagged `ivolve-app:lab8`.

Replace `YOUR_DOCKERHUB_USERNAME` with your Docker Hub username.

```bash
docker build -t ivolve-app:lab8 .
docker tag ivolve-app:lab8 YOUR_DOCKERHUB_USERNAME/ivolve-app:lab8
docker login
docker push YOUR_DOCKERHUB_USERNAME/ivolve-app:lab8
```

If you use the image already built by Docker Compose, you can tag it directly instead of rebuilding it:

```bash
docker tag ivolve-app:lab8 YOUR_DOCKERHUB_USERNAME/ivolve-app:lab8
docker push YOUR_DOCKERHUB_USERNAME/ivolve-app:lab8
```

**Security note:** Ensure `.dockerignore` is in place before building or publishing the image so `.env` is not included. If the image was built before `.dockerignore` was added, rebuild it and push the updated image.

## Stop the Application

Stop and remove the containers and Compose-created network:

```bash
docker compose down
```

To stop the stack and also remove its named volumes:

```bash
docker compose down -v
```

**Warning:** The `-v` option deletes the named volumes, including the persisted MySQL data and application logs. Use it only when you intentionally want to remove that data.

## Key Learning Outcomes

After completing this lab, you should be able to:

- Build a container image for a Node.js application.
- Orchestrate multiple services with Docker Compose.
- Configure service-to-service communication through a custom bridge network.
- Manage application configuration through environment variables.
- Persist database data and application logs with named volumes.
- Use health checks to coordinate service startup.
- Verify container health, logs, networking, and storage.
- Tag and publish a Docker image to Docker Hub.
