# ============================
# 1) Dependencies Stage
# ============================
FROM gradle:8.12.1-jdk21 AS dependencies

# Atualizar sistema e instalar dependências
RUN apt-get update && apt-get upgrade -y && apt-get install -y \
    libfreetype6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/* \
    && apt-get clean

WORKDIR /app

ENV GRADLE_OPTS="-Dhttps.proxyHost= -Dhttps.proxyPort= -Dhttp.proxyHost= -Dhttp.proxyPort="

# Copiar arquivos de configuração do Gradle
COPY build.gradle settings.gradle gradle.properties* ./
COPY gradle/ ./gradle/

# Download dependencies com cache
RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    gradle dependencies --no-daemon

# ============================
# 2) Build Stage
# ============================
FROM dependencies AS build

# Copiar código fonte
COPY src/ ./src/

# Build com cache otimizado
RUN --mount=type=cache,target=/root/.gradle/caches \
    --mount=type=cache,target=/root/.gradle/wrapper \
    gradle clean compileJasperReports copyCompiledJasper build -x test --no-daemon

# ============================
# 3) Runtime Stage
# ============================
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Copiar JAR do estágio anterior
COPY --from=build /app/build/libs/registration-0.0.1-SNAPSHOT.jar /app/app.jar

# Configurações JVM otimizadas
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java  -jar /app/app.jar"]
