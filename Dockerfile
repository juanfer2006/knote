FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app
COPY gradlew ./
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src
RUN chmod +x gradlew && ./gradlew bootJar --no-daemon

FROM eclipse-temurin:17-jre-alpine
WORKDIR /opt
ENV PORT=8080
EXPOSE 8080
COPY --from=build /app/build/libs/*.jar /opt/app.jar
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /opt/app.jar"]
