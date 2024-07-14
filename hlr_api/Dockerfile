#FROM eclipse-temurin:17-jdk-alpine
FROM arm64v8/openjdk:17-jdk
EXPOSE 8092
ADD target/hlr_api-0.0.1-SNAPSHOT.jar hlr_api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=k8s","-jar","/hlr_api-0.0.1-SNAPSHOT.jar"]