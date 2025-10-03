from bellsoft/liberica-openjdk-alpine:25 as builder
workdir /app
copy . .
run ./gradlew bootJar

from bellsoft/liberica-openjre-alpine:25
workdir /app
copy --from=builder /app/build/libs/*.jar app.jar
entrypoint ["java", "-jar", "/app/app.jar"]