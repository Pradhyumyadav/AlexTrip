package service;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DatabaseConnectionManager {
    private static HikariDataSource dataSource;

    // Private constructor to prevent instantiation
    private DatabaseConnectionManager() {
    }

    public static synchronized DataSource getDataSource() {
        if (dataSource == null) {
            // Fetch database credentials from environment variables
            String dbUrl = System.getenv("JDBC_DATABASE_URL");
            String username = System.getenv("JDBC_DATABASE_USERNAME");
            String password = System.getenv("JDBC_DATABASE_PASSWORD");

            if (dbUrl == null || username == null || password == null) {
                throw new IllegalStateException("Database configuration environment variables are not set.");
            }

            // Configure HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(dbUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);
        }
        return dataSource;
    }

    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}