FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY share_module-1.0.jar .
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN dos2unix mvnw
RUN ./mvnw install:install-file -Dfile="share_module-1.0.jar" -DgroupId="sn.esmt" -DartifactId=spoc -Dversion="1.0-SNAPSHOT" -Dpackaging=jar
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","sn.esmt.gesb.tpo_manager.TpoManagerApplication"]