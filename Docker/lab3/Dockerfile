FROM maven:3.9-eclipse-temurin-17

RUN useradd --create-home --shell /bin/bash appuser

WORKDIR /app

COPY --chown=appuser:appuser source/pom.xml .
COPY --chown=appuser:appuser source/src ./src

USER appuser

RUN mvn package

EXPOSE 8080

CMD ["java", "-jar", "target/calculator.jar"]
