FROM eclipse-temurin:17

WORKDIR /app


COPY target/clinic-api-0.0.1-SNAPSHOT.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/openlab.jar"]