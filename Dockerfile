FROM maven:3.8.2-openjdk-17 AS build

COPY ..
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/Quiz-Management-System-0.0.1.war /app.war

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "/app.war"]
