FROM eclipse-temurin:17-jdk-jammy as builder

WORKDIR /workspace


COPY pom.xml .
RUN mvn dependency:go-offline -B


COPY src ./src


RUN mvn package -DskipTests -B


FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
RUN useradd -m -s /bin/bash springuser
USER springuser


COPY --from=builder /workspace/target/clinic-api-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]