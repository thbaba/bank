from bellsoft/liberica-openjdk-alpine:25 as builder
workdir /app
copy . .
run ./gradlew bootJar

from bellsoft/liberica-openjre-alpine:25
run apk add --no-cache curl
workdir /app
copy --from=builder /app/build/libs/*.jar app.jar
entrypoint ["java", "-jar", "/app/app.jar"]