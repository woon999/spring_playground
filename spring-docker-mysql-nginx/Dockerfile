FROM openjdk:11
VOLUME /tmp
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} spring-nginx-mysql-app.jar
ENTRYPOINT ["java", "-jar", "spring-nginx-mysql-app.jar"]