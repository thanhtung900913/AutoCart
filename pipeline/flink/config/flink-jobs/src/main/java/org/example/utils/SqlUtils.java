package org.example.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SqlUtils {
    private static final Logger LOG = LoggerFactory.getLogger(SqlUtils.class);

    public static List<String> readSqlStatements(String resourcePath) {
        LOG.info("Loading SQL script from: {}", resourcePath);
        try (InputStream is = SqlUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new RuntimeException("SQL file not found: " + resourcePath);
            }
            String content = new BufferedReader(new InputStreamReader(is))
                    .lines().collect(Collectors.joining("\n"));
            // Split SQL statement by semicolon
            String[] statements = content.split(";");
            List<String> validStatements = new ArrayList<>();
            for (String stmt : statements) {
                if (!stmt.trim().isEmpty()) {
                    validStatements.add(stmt.trim());
                }
            }
            return validStatements;
        } catch (Exception e) {
            LOG.error("Error while loading SQL file: {}", resourcePath, e);
            throw new RuntimeException(e);
        }
    }
}
