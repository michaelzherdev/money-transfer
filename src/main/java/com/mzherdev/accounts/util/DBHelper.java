package com.mzherdev.accounts.util;

import org.apache.log4j.Logger;
import org.h2.tools.RunScript;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBHelper {

    private static final Logger log = Logger.getLogger(DBHelper.class);

    private static final String DB_DRIVER = ConfigLoader.getProperty("db_driver");
    private static final String DB_CONNECTION_URL = ConfigLoader.getProperty("db_connection_url");
    private static final String DB_USER = ConfigLoader.getProperty("db_user");
    private static final String DB_PASSWORD = ConfigLoader.getProperty("db_password");

    private DBHelper() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("Error during loading driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION_URL, DB_USER, DB_PASSWORD);

    }

    public static void populateTestData() {
        try (Connection connection = getConnection()) {
            RunScript.execute(connection, new FileReader("src/main/resources/data.sql"));
        } catch (FileNotFoundException | SQLException e) {
            log.error("Error reading file: ", e);
            throw new RuntimeException(e);
        }
    }
}
