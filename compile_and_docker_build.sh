#!/bin/bash
mvn clean compile assembly:single
docker build -t sample-batch-job .
docker tag sample-batch-job:latest pomtcom/java-batch-docker-sample:s3
#docker run --rm sample-batch-job