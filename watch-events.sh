#!/bin/bash

oc -n drivenow-production rsh kafka-schema-registry-0 kafka-avro-console-consumer --bootstrap-server kafka-kafka-bootstrap:9092 --topic divolte