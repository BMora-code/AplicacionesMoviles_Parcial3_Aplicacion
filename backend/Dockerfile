FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Copiar archivos de Maven
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY src src

# Construcción
RUN chmod +x mvnw && ./mvnw clean package -DskipTests

# Runtime
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

COPY --from=0 /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
