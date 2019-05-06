#!/bin/bash
docker-compose exec schema-registry kafka-avro-console-consumer \
    --bootstrap-server kafka:9092 \
    --topic divolte