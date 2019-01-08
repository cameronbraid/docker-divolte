#!/bin/bash
curl "${SCHEMA_REGISTRY:-http://localhost:8081}/subjects/divolte-value/versions/latest"
