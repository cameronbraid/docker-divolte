#!/bin/bash

if [ -z "$SCHEMA_REGISTRY" ]; then
  echo "SCHEMA_REGISTRY env not, expecting something like http://schema-registry.example.com:8081"
  exit -2
fi

echo "Deleting divolte"
curl -X DELETE "${SCHEMA_REGISTRY}/subjects/divolte-value"
