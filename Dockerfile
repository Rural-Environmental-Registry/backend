# ============================
# 1) Dependencies Stage
# ============================
FROM eclipse-temurin:21-jdk-jammy AS dependencies

RUN apt-get update && apt-get install -y --no-install-recommends \
    libfreetype6 \
    fontconfig \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

ENV GRADLE_OPTS="-Dhttps.proxyHost= -Dhttps.proxyPort= -Dhttp.proxyHost= -Dhttp.proxyPort="

# Copiar arquivos de configuração do Gradle (wrapper included)
COPY build.gradle settings.gradle gradle.properties* ./
COPY gradle/ ./gradle/
COPY --chmod=755 gradlew ./

# Download dependencies com cache
RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    ./gradlew dependencies --no-daemon

# ============================
# 2) Build Stage
# ============================
FROM dependencies AS build

COPY src/ ./src/

RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    ./gradlew build -x test --no-daemon

# ============================
# 3) Runtime Stage
# ============================
FROM eclipse-temurin:21-jre-jammy AS runtime

RUN addgroup --system --gid 1001 appgroup && \
    adduser --system --uid 1001 --ingroup appgroup appuser

WORKDIR /app

COPY --from=build --chown=appuser:appgroup /app/build/libs/registration-0.0.1-SNAPSHOT.jar /app/app.jar

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

USER appuser
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
