from bellsoft/liberica-openjdk-alpine:25 as builder
workdir /app
copy . .
run ./mvnw clean package -DskipTests

from bellsoft/liberica-openjre-alpine:25
workdir /app
copy --from=builder /app/target/*.jar app.jar
entrypoint ["java", "-jar", "/app/app.jar"]