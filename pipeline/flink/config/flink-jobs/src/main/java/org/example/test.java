package org.example;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

public class test {
    public static void main(String[] args) {
        // 1. Khởi tạo môi trường Flink Table API ở chế độ Streaming
        EnvironmentSettings settings = EnvironmentSettings.newInstance().inStreamingMode().build();
        TableEnvironment tEnv = TableEnvironment.create(settings);

        System.out.println("⏳ Đang khởi tạo bảng Nguồn (Tự sinh dữ liệu giả)...");

        // 2. Tạo Source Table dùng 'datagen'
        // Đã THÊM dấu backtick (`) vào tên cột để tránh trùng từ khóa hệ thống
        tEnv.executeSql(
                "CREATE TABLE source_t_user (" +
                        "    `user_id` BIGINT," +
                        "    `user_type` INT," +
                        "    `language` STRING," +
                        "    `score` DOUBLE" +
                        ") WITH (" +
                        "    'connector' = 'datagen'," +
                        "    'rows-per-second' = '1'," +
                        "    'fields.user_id.min' = '1'," +
                        "    'fields.user_id.max' = '1000'," +
                        "    'fields.language.length' = '2'" +
                        ")"
        );

        System.out.println("⏳ Đang khởi tạo bảng Đích (ClickHouse)...");

        // 3. Tạo Sink Table trỏ tới ClickHouse
        tEnv.executeSql(
                "CREATE TABLE sink_t_user (" +
                        "    `user_id` BIGINT," +
                        "    `user_type` INT," +
                        "    `language` STRING," +
                        "    `score` DOUBLE," +
                        "    PRIMARY KEY (`user_id`) NOT ENFORCED" +
                        ") WITH (" +
                        "    'connector' = 'clickhouse'," +
                        "    'url' = 'clickhouse://localhost:8123'," +  // Sửa lại host nếu cần
                        "    'database-name' = 'default'," +
                        "    'table-name' = 't_user'," +
                        "    'username' = 'admin'," +                   // Sửa lại username
                        "    'password' = 'admin_password'," +          // Sửa lại password
                        "    'sink.batch-size' = '2'," +
                        "    'sink.flush-interval' = '1000'," +
                        "    'sink.max-retries' = '3'" +
                        ")"
        );

        System.out.println("🚀 Đang Submit Job: Đổ dữ liệu từ DataGen vào ClickHouse...");

        // 4. Thực thi việc cắm ống hút từ Source sang Sink
        tEnv.executeSql("INSERT INTO sink_t_user SELECT * FROM source_t_user");
    }
}