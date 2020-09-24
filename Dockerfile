FROM alpine:latest
ADD target/batch-docker-demo-full-sample1-1.0-SNAPSHOT-jar-with-dependencies.jar batch-docker-demo-full-sample1-1.0-SNAPSHOT-jar-with-dependencies.jar
ADD sample_data.csv sample_data.csv
RUN apk --update add openjdk8-jre
ENTRYPOINT ["java", "-jar", "batch-docker-demo-full-sample1-1.0-SNAPSHOT-jar-with-dependencies.jar"]