# Use a Maven image to build the application
FROM maven:3.8.2-openjdk-17 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the source code into the container
COPY . /app

# Run Maven to build the application and skip tests
RUN mvn clean package -DskipTests

# Use a smaller OpenJDK image for the final stage
FROM openjdk:17.0.1-jdk-slim

# Copy the built .war file from the build stage
COPY --from=build /app/target/Quiz-Management-System-0.0.1.war /app.war

# Expose the port the application will run on
EXPOSE 8083

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "/app.war"]
