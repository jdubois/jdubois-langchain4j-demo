#!/bin/bash
docker buildx build --platform linux/amd64 -t jdubois/jdubois-langchain4j-demo:9 --push .
