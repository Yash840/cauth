# Stage 1: build with Maven on JDK 25
FROM maven:3.9.9-eclipse-temurin-25 AS build
WORKDIR /workspace
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN mvn -B -f pom.xml dependency:go-offline

COPY src ./src
RUN mvn -B -DskipTests package

# Stage 2: runtime (slim JRE 25)
FROM eclipse-temurin:25-jre-jammy
WORKDIR /app
COPY docker/wait-for.sh /app/wait-for.sh
RUN chmod +x /app/wait-for.sh
COPY --from=build /workspace/target/*.jar /app/auth-service.jar

EXPOSE 8085
ENTRYPOINT ["/app/wait-for.sh", "postgres", "5432", "java", "-jar", "/app/auth-service.jar"]
