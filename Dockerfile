FROM openjdk:8
ADD target/mile-restoraunt-app-0.0.1-SNAPSHOT.jar mile-restoraunt-app-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar" , "mile-restoraunt-app-0.0.1-SNAPSHOT.jar"]