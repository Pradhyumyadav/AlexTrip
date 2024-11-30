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

            String dbUrl = dotenv.get("DATABASE_URL");
            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new RuntimeException("DATABASE_URL not found in .env file.");
            }

            URI dbUri = new URI(dbUrl);

            // Extract the username, password, and JDBC URL from the URI
            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String jdbcUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() + "?sslmode=require";

            // Configure the HikariDataSource
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(jdbcUrl);
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.addDataSourceProperty("ssl", "true");
            config.addDataSourceProperty("sslfactory", "org.postgresql.ssl.NonValidatingFactory");
            config.setInitializationFailTimeout(0); // Allow pool to start even if initial connection fails

            dataSource = new HikariDataSource(config);

        } catch (URISyntaxException | ClassNotFoundException e) {
            throw new RuntimeException("Error initializing database connection: " + e.getMessage(), e);
        }
    }

    /**
     * Returns a Connection object from the connection pool.
     *
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Returns the DataSource object for connection pooling.
     *
     * @return DataSource object
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
}