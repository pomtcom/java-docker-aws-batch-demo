#!/bin/bash
mvn clean compile assembly:single
java -jar /Users/s90623/Desktop/git_code/batch-docker-demo-full-sample1/target/batch-docker-demo-full-sample1-1.0-SNAPSHOT-jar-with-dependencies.jar