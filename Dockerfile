
FROM openjdk

ARG JAR_FILE=target/*.jar

COPY ./target/basket-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
