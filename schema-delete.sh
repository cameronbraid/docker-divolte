#!/bin/bash
echo "Deleting divolte"
curl -X DELETE "${SCHEMA_REGISTRY:-http://localhost:8081}/subjects/divolte-value"
