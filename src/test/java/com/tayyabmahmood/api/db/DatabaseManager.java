package com.tayyabmahmood.api.db;

import com.tayyabmahmood.api.config.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

// H2 in-memory for now. Point db.url at Oracle/Postgres to use a real DB.
public final class DatabaseManager {

    private static volatile boolean initialised;

    private DatabaseManager() {
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(
                ConfigManager.get("db.url"),
                ConfigManager.get("db.user"),
                ConfigManager.get("db.password"));
        initialiseOnce(connection);
        return connection;
    }

    private static void initialiseOnce(Connection connection) throws SQLException {
        if (initialised || !Boolean.parseBoolean(ConfigManager.get("db.seed"))) {
            return;
        }
        synchronized (DatabaseManager.class) {
            if (!initialised) {
                try (Statement st = connection.createStatement()) {
                    st.execute("RUNSCRIPT FROM 'classpath:db/schema.sql'");
                    st.execute("RUNSCRIPT FROM 'classpath:db/data.sql'");
                }
                initialised = true;
            }
        }
    }
}
