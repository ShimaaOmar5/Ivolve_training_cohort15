# Lab 2: Java Calculator Using Gradle

## Objective
Build and test a Java calculator application using Gradle as the build automation tool.

## Technologies
- Java 17
- Gradle 9.8.2
- Groovy DSL
- JUnit Jupiter 6.0.1

## Project Structure
```text
lab2/
├── app/
│   ├── src/
│   │   ├── main/java/org/example/App.java
│   │   └── test/java/org/example/AppTest.java
│   └── build.gradle
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
├── gradlew
├── gradlew.bat
├── gradle.properties
└── settings.gradle
```

## Build and Test
Run the following command from the lab2 directory:

```bash
./gradlew clean build
```

## Run the Application
```bash
java -jar app/build/libs/calculator.jar
```

## Example Output
```text
Enter first number: 5
Enter second number: 15
a = 5.0, b = 15.0
Sum: 20.0
Subtract: -10.0
Multiply: 75.0
Divide: 0.3333333333333333
```

## Result
The Gradle build completed successfully, and the calculator application ran successfully.
