# ============================
# 1) Build Stage
# ============================
FROM gradle:8.12.1-jdk21 AS builder
WORKDIR /app

# ENV GRADLE_OPTS="-Dhttps.proxyHost=10.31.220.23 -Dhttps.proxyPort=3128 -Dhttp.proxyHost=10.31.220.23 -Dhttp.proxyPort=3128"

# Copy the project files into the container
COPY . .

# Build the application (skip tests if you want faster builds in dev)
# RUN gradle clean build -x test 
RUN chmod +x ./gradlew
RUN ./gradlew clean compileJasperReports copyCompiledJasper 
RUN ./gradlew build -x test

# ============================
# 2) Run Stage
# ============================
FROM openjdk:21-slim
WORKDIR /app

RUN apt-get update && apt-get install -y \
  libfreetype6 \
  fontconfig \
  && rm -rf /var/lib/apt/lists/*


# Copy the built JAR from the build stage into the runtime image
COPY --from=builder /app/build/libs/*.jar /app/app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Set the entrypoint to run the jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
