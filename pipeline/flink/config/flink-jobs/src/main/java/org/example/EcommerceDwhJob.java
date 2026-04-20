package org.example;
import org.apache.commons.text.StringSubstitutor;
import org.apache.flink.configuration.MemorySize;
import org.apache.flink.configuration.TaskManagerOptions;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.StatementSet;
import org.apache.flink.table.api.TableEnvironment;
import org.example.config.ConfigLoader;
import org.example.utils.SqlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.flink.configuration.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EcommerceDwhJob {
    private static final Logger LOG = LoggerFactory.getLogger(EcommerceDwhJob.class);

    public static void main(String[] args) {
        LOG.info("Flink job is running ...");

        try {
            Configuration configuration = new Configuration();
            // 2. Increase Network Memory (The defaults are usually too small for complex local testing)
            // Increase the minimum network memory (e.g., to 128MB or 256MB)
            configuration.set(TaskManagerOptions.NETWORK_MEMORY_MIN, MemorySize.parse("256mb"));
            // Increase the maximum network memory
            configuration.set(TaskManagerOptions.NETWORK_MEMORY_MAX, MemorySize.parse("256mb"));

            // 2. Load Cấu hình
            ConfigLoader config = ConfigLoader.getInstance();

            EnvironmentSettings settings = EnvironmentSettings.newInstance()
                    .inStreamingMode()
                    .withConfiguration(configuration)
                    .build();
            TableEnvironment tEnv = TableEnvironment.create(settings);
            tEnv.getConfig().set("table.exec.state.ttl", "1 d");

            // 3. Đọc và thực thi Source Tables (Kafka)
            LOG.info("Đang khởi tạo các Kafka Source Tables...");
            executeSqlFile(tEnv, "sql/1_sources.sql", config);

            // 4. Đọc và thực thi Sink Tables (ClickHouse - Đã kèm Retry)
            LOG.info("Đang khởi tạo các ClickHouse Sink Tables...");
            executeSqlFile(tEnv, "sql/2_sinks.sql", config);

            // 5. Đọc và nạp Pipeline vào StatementSet
            LOG.info("Đang chuẩn bị luồng dữ liệu (Pipelines)...");
            StatementSet stmtSet = tEnv.createStatementSet();
            List<String> pipelineStmts = SqlUtils.readSqlStatements("sql/3_pipelines.sql");

            for (String stmt : pipelineStmts) {
                stmtSet.addInsertSql(stmt);
            }

            // 6. Submit Job
            LOG.info("⚡ Đang Submit toàn bộ Pipeline lên Flink Cluster...");
            stmtSet.execute();
            LOG.info("✅ Submit thành công! Job đang chạy ngầm.");

        } catch (Exception e) {
            LOG.error("❌ Hệ thống gặp lỗi nghiêm trọng (Fatal Error) và phải dừng lại!", e);
            System.exit(1);
        }
    }

    /**
     * Hàm phụ trợ: Đọc file SQL, replace biến môi trường và thực thi từng lệnh
     */
    private static void executeSqlFile(TableEnvironment tEnv, String filePath, ConfigLoader config) {
        List<String> statements = SqlUtils.readSqlStatements(filePath);

        // 1. Tạo một Map chứa tất cả các biến bạn muốn truyền vào SQL
        Map<String, String> valuesMap = new HashMap<>();
        valuesMap.put("KAFKA_BOOTSTRAP_SERVERS", config.kafkaServers);
        valuesMap.put("CLICKHOUSE_URL", config.chUrl);
        valuesMap.put("CLICKHOUSE_USER", config.chUser);
        valuesMap.put("CLICKHOUSE_PASSWORD", config.chPassword);

        // 2. Tạo cỗ máy thay thế (Substitutor)
        StringSubstitutor sub = new StringSubstitutor(valuesMap);

        for (String stmt : statements) {
            // 3. Cỗ máy tự động tìm mọi chữ ${...} trong SQL và thay bằng giá trị trong Map
            String finalStmt = sub.replace(stmt);

            LOG.debug("Thực thi SQL:\n{}", finalStmt);
            tEnv.executeSql(finalStmt);
        }
    }
}