package mg.framework.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DbConnection {
    private static final String PROPERTIES_FILE = "/application.properties";
    private static final Properties PROPERTIES = loadProperties();

    private DbConnection() {
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC driver not found", e);
        }

        String url = firstNonEmpty(
                PROPERTIES.getProperty("db.url"),
                PROPERTIES.getProperty("spring.datasource.url")
        );
        String username = firstNonEmpty(
                PROPERTIES.getProperty("db.username"),
                PROPERTIES.getProperty("spring.datasource.username")
        );
        String password = firstNonEmpty(
                PROPERTIES.getProperty("db.password"),
                PROPERTIES.getProperty("spring.datasource.password"),
                ""
        );

        if (url == null || username == null) {
            throw new SQLException("Database configuration missing in application.properties");
        }

        return DriverManager.getConnection(url, username, password);
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = DbConnection.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        } catch (IOException ignored) {
            // Keep empty properties if file is not found
        }
        return properties;
    }

    private static String firstNonEmpty(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
