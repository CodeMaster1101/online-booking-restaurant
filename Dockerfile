FROM openjdk:8
LABEL key="Mile Stanislavov"
ADD target/mile-restoraunt-app-0.0.1-SNAPSHOT.jar restaurant_app.jar
ENTRYPOINT [ "java", "-jar", "restaurant_app.jar" ]