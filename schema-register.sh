#!/bin/bash


echo "Registring divolte.avsc"
SCHEMA=`echo -n '{"schema":'; cat ./conf/divolte.avsc | sed -e "s/\/\/.*//g" | jq ". | tojson"; echo  -n '}'`
echo $SCHEMA
curl -i -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "$SCHEMA" "${SCHEMA_REGISTRY:-http://localhost:8081}/subjects/divolte-value/versions"
