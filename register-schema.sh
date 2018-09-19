#!/bin/bash

if [ -z "$SCHEMA_REGISTRY" ]; then
  echo "SCHEMA_REGISTRY env not, expecting something like http://schema-registry.example.com:8081"
  exit -2
fi

echo "Registring divolte.avsc"
SCHEMA=`echo -n '{"schema":'; cat ./conf/divolte.avsc | sed -e "s/\/\/.*//g" | jq ". | tojson"; echo  -n '}'`
echo $SCHEMA
curl -i -H "Content-Type: application/vnd.schemaregistry.v1+json" --data "$SCHEMA" "${SCHEMA_REGISTRY}/subjects/divolte-value/versions"
