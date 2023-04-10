FROM maven:3.9-eclipse-temurin-17-alpine AS build
LABEL "author"="Emmanuel K"
WORKDIR /app/backend
COPY . .
ENTRYPOINT ["mvn", "spring-boot:run", "-P", "docker"]
EXPOSE 8080

