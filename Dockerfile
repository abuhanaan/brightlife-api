FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY logic/target/logic-0.0.1-SNAPSHOT.jar bright-life-api.jar
ENTRYPOINT ["java","-jar","/bright-life-api.jar"]