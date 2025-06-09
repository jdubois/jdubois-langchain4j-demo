FROM mcr.microsoft.com/openjdk/jdk:21-mariner AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM mcr.microsoft.com/openjdk/jdk:21-mariner
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
COPY azure-cli-mcp.jar .

# Install Azure CLI
RUN tdnf install azure-cli -y && \
    tdnf clean all

# Default port - can be overridden
ENV SERVER_PORT=80

ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.demo.DemoApplication"]
