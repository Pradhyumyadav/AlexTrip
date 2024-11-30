package utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {
    private static final Connection connection;

    static {
        try {
            String dbUrl = System.getenv("DATABASE_URL");
            if (dbUrl == null || dbUrl.isEmpty()) {
                throw new RuntimeException("DATABASE_URL environment variable not set.");
            }

            URI dbUri = new URI(dbUrl);

            String username = dbUri.getUserInfo().split(":")[0];
            String password = dbUri.getUserInfo().split(":")[1];
            String dbUrlForDriver = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

            connection = DriverManager.getConnection(dbUrlForDriver, username, password);
        } catch (URISyntaxException | SQLException e) {
            throw new RuntimeException("Error setting up database connection: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}