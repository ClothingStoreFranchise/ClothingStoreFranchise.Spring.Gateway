FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8080

ADD target/gateway.jar gateway.jar

ENTRYPOINT ["java", "-jar", "gateway.jar"]