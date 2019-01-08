# Divolte
#
# Divolte Documentation:
# www.divolte.io
#

FROM openjdk:8-jre-slim

#
# Configuration
#

ARG BUILD_DATE
ARG DIVOLTE_VERSION=0.8.0
ARG DIVOLTE_CHECKSUM=4bbd88cffebbb36beeb3c82008adce6db36b76263211ea43fd77cd8e80badb32
ARG ENABLE_KERBEROS=no
ENV ENABLE_KERBEROS=$ENABLE_KERBEROS


LABEL org.label-schema.name="Divolte ${DIVOLTE_VERSION}" \
      org.label-schema.build-date=$BUILD_DATE \
      org.label-schema.version=$DIVOLTE_VERSION

#
# Install dependencies and Divolte
#

RUN apt-get update && \
    if [ "$ENABLE_KERBEROS" = "yes" ]; then\
        apt-get install -y --allow-unauthenticated curl krb5-user;\
    else\
        apt-get install -y --allow-unauthenticated curl;\
    fi && \
    mkdir -p /opt/divolte && \
    cd /tmp/ && \
    curl -O http://divolte-releases.s3-website-eu-west-1.amazonaws.com/divolte-collector/${DIVOLTE_VERSION}/distributions/divolte-collector-${DIVOLTE_VERSION}.tar.gz && \
    echo "${DIVOLTE_CHECKSUM}  divolte-collector-${DIVOLTE_VERSION}.tar.gz" | sha256sum -c - && \
    tar zxpf divolte-collector-${DIVOLTE_VERSION}.tar.gz -C /opt/divolte && \
    mv /opt/divolte/divolte-collector-${DIVOLTE_VERSION}/ /opt/divolte/divolte-collector && \
    apt-get autoremove -y && \
    apt-get clean -y && \
    rm -fr /var/tmp/* /tmp/*

#
# Configuration changes using divolte-collector.conf
#
COPY conf/ /opt/divolte/divolte-collector/conf/

COPY configureKerberosClient.sh /opt/divolte/
COPY start.sh /opt/divolte/

#
# Expose the Divolte Collector click simulation web page
#
EXPOSE 8290
WORKDIR /opt/divolte/
CMD ["/opt/divolte/start.sh"]
