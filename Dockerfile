# Use an official Maven image to build the app
FROM maven:3.8.4-openjdk-8 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download dependencies
COPY pom.xml /app
RUN mvn dependency:go-offline

# Copy the source code into the container
COPY src /app/src

# Install the application
RUN mvn install -DskipTests

# Use an official OpenJDK runtime as a parent image
FROM openjdk:8-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/target/mile-restaurant-app-0.0.2-SNAPSHOT.jar /app/mile-restaurant-app-0.0.2-SNAPSHOT.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "/app/mile-restaurant-app-0.0.2-SNAPSHOT.jar"]
