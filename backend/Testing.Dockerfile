FROM maven:3.9-eclipse-temurin-17-alpine AS build
LABEL "author"="Emmanuel K"
WORKDIR /app/backend
COPY . .
ENTRYPOINT ["mvn", "test", "-e"]

