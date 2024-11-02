FROM openjdk:21
ARG JAR_FILE=target/bank-service.jar
COPY ${JAR_FILE} bank-service.jar
ENTRYPOINT ["java", "-jar", "/bank-service.jar"]