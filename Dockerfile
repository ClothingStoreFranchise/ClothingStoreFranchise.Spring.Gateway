FROM adoptopenjdk/openjdk11:alpine-jre

EXPOSE 8080

ADD target/discovery.jar discovery.jar

ENTRYPOINT ["java", "-jar", "discovery.jar"]