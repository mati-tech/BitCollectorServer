# Stage 1: Build the application
FROM gradle:8.8-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/BitCollectorServer.jar
COPY src/main/resources/static /app/static
ENTRYPOINT ["java", "-jar", "/app/BitCollectorServer.jar"]