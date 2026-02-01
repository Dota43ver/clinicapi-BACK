# Etapa 1: Construcción (Build)
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copiamos los archivos del proyecto
COPY . .

# Corregimos los finales de línea de Windows (CRLF) a Linux (LF) 
# y damos permisos de ejecución al wrapper de Maven
RUN tr -d '\r' < mvnw > mvnw_unix && \
    mv mvnw_unix mvnw && \
    chmod +x mvnw

# Ejecutamos la construcción del JAR saltando los tests
RUN ./mvnw package -DskipTests

# Etapa 2: Ejecución (Runtime)
FROM eclipse-temurin:21-jre

WORKDIR /app

# Definimos el puerto (Railway lo asigna dinámicamente)
ARG PORT
ENV PORT=${PORT}

# Copiamos solo el JAR generado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Creamos un usuario no-root por seguridad
RUN useradd -m runtime
USER runtime

# Ejecutamos la aplicación
# Usamos el puerto que Railway nos pasa por variable de entorno
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "app.jar" ]