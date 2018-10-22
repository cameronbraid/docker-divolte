#!/bin/bash
echo "Getting divolte"
curl "${SCHEMA_REGISTRY:-http://localhost:8081}/subjects/divolte-value/versions/latest"
