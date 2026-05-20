# Frontend build stage
FROM node:25 AS frontend-build
WORKDIR /app/src/frontend
COPY src/frontend/package*.json ./
RUN npm ci
COPY src/frontend ./
RUN npm run build

#Java build stage
FROM eclipse-temurin:26-jdk AS build
WORKDIR /app
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ls -r .
RUN ./mvnw dependency:go-offline
COPY src ./src
COPY --from=frontend-build /app/src/frontend/dist ./src/main/resources/static
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:26-jre
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
