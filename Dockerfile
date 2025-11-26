# You need to set the envionment variables for the application to start correctly:
# OPENAI_BASE_URL
 # OPENAI_API_KEY
 # AZURE_SEARCH_ENDPOINT
 # AZURE_SEARCH_KEY

FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:25-jre
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", \
    "-XX:TieredStopAtLevel=1", \
    "-XX:+UseParallelGC", \
    "-XX:+DisableExplicitGC", \
    "--enable-native-access=ALL-UNNAMED", \
    "--add-opens=java.base/java.lang=ALL-UNNAMED", \
    "--add-opens=jdk.unsupported/sun.misc=ALL-UNNAMED", \
    "--add-exports=jdk.unsupported/sun.misc=ALL-UNNAMED", \
    "-Dserver.address=0.0.0.0", \
    "-cp", "app:app/lib/*", \
    "com.example.demo.DemoApplication"]

EXPOSE 8080
