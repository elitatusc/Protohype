FROM ubuntu:latest
LABEL authors="elitatusc"
RUN apt-get update && apt-get install -y openjdk-17-jdk
WORKDIR /app
COPY target/demo-1.0-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]