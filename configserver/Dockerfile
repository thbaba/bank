from bellsoft/liberica-openjdk-alpine:24.0.1 as builder
workdir /app
copy . .
run ./gradlew bootJar

from bellsoft/liberica-openjre-alpine:24.0.1
run apk add --no-cache curl
workdir /app
copy --from=builder /app/build/libs/*.jar app.jar
expose 80
entrypoint ["java", "-jar", "/app/app.jar"]