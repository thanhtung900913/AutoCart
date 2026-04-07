FROM flink:1.18.1

# install Python and Java
RUN apt-get update -y && \
    apt-get install -y --no-install-recommends \
        python3 \
        python3-pip \
        python3-dev \
        openjdk-21-jdk-headless \
        ant && \
    ln -s /usr/bin/python3 /usr/bin/python && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# set JAVA_HOME environment variable
ENV JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64

WORKDIR /opt/flink

# install Python dependencies
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && \
    pip install --no-cache-dir -r requirements.txt

# install Flink connectors
COPY download_libs.sh .
RUN chmod +x ./download_libs.sh && ./download_libs.sh

# copy Flink job JARs
COPY ./jobs/ ./jobs