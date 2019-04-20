#!/bin/bash
docker-compose -f ../drivenow-webapp/docker-compose.yml exec schema-registry kafka-avro-console-consumer \
    --bootstrap-server kafka:9092 \
    --topic divolte | jq --unbuffered -R -r '. as $line | try fromjson catch $line'