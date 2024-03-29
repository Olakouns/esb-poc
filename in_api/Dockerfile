FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8091
ADD target/in_api-0.0.1-SNAPSHOT.jar in_api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=k8s","-jar","/in_api-0.0.1-SNAPSHOT.jar"]