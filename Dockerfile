FROM maven:3.9.5-eclipse-temurin-21-alpine as builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

ENV JAVA_TOOL_OPTIONS="-Djava.net.preferIPv4Stack=true"

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
