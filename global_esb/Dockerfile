FROM eclipse-temurin:17-jdk-alpine
EXPOSE 8093
ADD target/global_esb-0.0.1-SNAPSHOT.jar global_esb-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Dspring.profiles.active=k8s","-jar","/global_esb-0.0.1-SNAPSHOT.jar"]