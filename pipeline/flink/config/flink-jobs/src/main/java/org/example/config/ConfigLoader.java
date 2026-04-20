package org.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLoader {
    private static final Logger LOG = LoggerFactory.getLogger(ConfigLoader.class);
    private static ConfigLoader instance;

    public final String kafkaServers;
    public final String chUrl;
    public final String chUser;
    public final String chPassword;

    private ConfigLoader() {
        LOG.info("Loading environment variable ...");
        this.kafkaServers = System.getenv().getOrDefault("KAFKA_BOOTSTRAP_SERVERS", "192.168.1.5:9092");
        this.chUrl = System.getenv().getOrDefault("CLICKHOUSE_URL", "clickhouse://localhost:8123");
        this.chUser = System.getenv().getOrDefault("CLICKHOUSE_USER", "admin");
        this.chPassword = System.getenv().getOrDefault("CLICKHOUSE_PASSWORD", "admin_password");

        LOG.info("Kafka Servers: {}", this.kafkaServers);
        LOG.info("ClickHouse URL: {}", this.chUrl);
    }

    public static synchronized ConfigLoader getInstance() {
        if (instance == null) {
            instance = new ConfigLoader();
        }
        return instance;
    }
}
