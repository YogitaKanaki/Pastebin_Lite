FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

RUN chmod +x gradlew
RUN ./gradlew bootJar -x test

# Copy jar with fixed name
RUN cp build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
