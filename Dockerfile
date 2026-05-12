FROM gradle:9.4.1-jdk21 AS build
WORKDIR /workspace

# Cache dependencies
COPY gradle gradle
COPY gradlew gradlew
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew --no-daemon dependencies

# Build application
COPY src src
RUN ./gradlew --no-daemon clean build -x test

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /workspace/build/libs/*.jar /app/app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]

