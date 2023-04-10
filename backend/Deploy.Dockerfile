# will use l8r ig

FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app/backend
COPY . .
RUN mvn clean package -DskipTests -Denv=cloud

FROM eclipse-temurin:17-alpine
LABEL "author"="Emmanuel K"
COPY --from=build /app/backend/target/pw-2023-seng-1.0.0.jar /usr/bin/pw-2023-seng-1.0.0.jar
ENTRYPOINT ["java", "-jar", "/usr/bin/pw-2023-seng-1.0.0.jar"]
EXPOSE 8080

