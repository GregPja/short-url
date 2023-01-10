FROM openjdk:17-jdk-slim

ADD ./build/libs/short-url-0.0.1-SNAPSHOT.jar short-url.jar

ENTRYPOINT exec java $JAVA_OPTS -jar short-url.jar

EXPOSE 8080