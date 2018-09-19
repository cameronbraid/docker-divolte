#!/bin/bash

if [ "$ENABLE_KERBEROS" = "yes" ]; then
  /opt/divolte/configureKerberosClient.sh
  export DIVOLTE_JAVA_OPTS="-Djava.security.krb5.conf=/etc/krb5.conf -Dsun.security.krb5.debug=true"
fi

DIVOLTE_HDFS_SINK_WORKING_DIR=${DIVOLTE_HDFS_SINK_WORKING_DIR:/tmp/working}
mkdir -p "$DIVOLTE_HDFS_SINK_WORKING_DIR"

DIVOLTE_HDFS_SINK_PUBLISH_DIR=${DIVOLTE_HDFS_SINK_PUBLISH_DIR:/tmp/processed}
mkdir -p "$DIVOLTE_HDFS_SINK_WORKING_DIR"

# if [ -z "$SCHEMA_REGISTRY" ]; then
#   echo "SCHEMA_REGISTRY env not, expecting something like http://schema-registry.example.com:8081"
#   exit -2
# fi

# echo "Registring divolte.avsc"
# (echo -n '{"schema":"'; cat /opt/divolte/divolte-collector/conf/divolte.avsc | sed -e "s/\/\/.*//g" | sed -e 's/"/\\"/g' | tr -d \\n; echo -n '"}' | tr -d \\n) > /tmp/schema.json 
# cat /tmp/schema.json

# curl -v -X POST -i -H "Content-Type: application/vnd.schemaregistry.v1+json" \
#     --data-binary "@/tmp/schema.json" \
#      "${SCHEMA_REGISTRY}/subjects/divolte-value/versions"

/opt/divolte/divolte-collector/bin/divolte-collector
