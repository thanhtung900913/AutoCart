FROM flink:1.18.1

WORKDIR /opt/flink

# install Flink connectors
COPY download_libs.sh .
RUN chmod +x ./download_libs.sh && ./download_libs.sh