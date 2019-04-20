#!/bin/bash
oc rsh kafka-schema-registry-0 kafka-avro-console-consumer \
    --bootstrap-server kafka-kafka-bootstrap:9092 \
    --topic divolte | jq --unbuffered -R -r '. as $line | try fromjson catch $line'