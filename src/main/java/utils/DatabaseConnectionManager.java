package utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final HikariDataSource dataSource;

    static {
        try {
            // Load the PostgreSQL driver
            Class.forName("org.postgresql.Driver");

            // Load environment variables from the .env file
            Dotenv dotenv = Dotenv.configure()
                    .directory("/Users/pradhyumyadav/IdeaProjects/AlexTripAgencyManagementSystem/src/main/java/service")
                    .load();

            // Get DATABASE_URL from the .env file
            String dbUrl = dotenv.get("DATABASE_URL");
            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new RuntimeException("DATABASE_URL not found in .env file.");
            }

            // Parse the DATABASE_URL
            URI dbUri = new URI(dbUrl);

            // Extract username, password, and JDBC URL components
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

            // Configure HikariCP (Connection Pooling)
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10); // Maximum connections in the pool
            config.setMinimumIdle(2);     // Minimum idle connections in the pool
            config.setIdleTimeout(30000); // 30 seconds before an idle connection is removed
            config.setMaxLifetime(1800000); // Maximum lifetime of a connection (30 minutes)
            config.setInitializationFailTimeout(0); // Allow the pool to start even if no DB connection is available
            config.addDataSourceProperty("ssl", "true"); // Enforce SSL
            config.addDataSourceProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");

            // Initialize the data source
            dataSource = new HikariDataSource(config);

        } catch (URISyntaxException | ClassNotFoundException e) {
            throw new RuntimeException("Error initializing database connection: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a connection from the connection pool.
     *
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Retrieves the DataSource object for connection pooling.
     *
     * @return DataSource object
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
}