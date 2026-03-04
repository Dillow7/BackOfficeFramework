package com.hotel.backoffice;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {
    private Db() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver not found", e);
        }
        String url = "jdbc:mysql://localhost:3306/hotel";
        String user = "root";
        String password = "root";
        return DriverManager.getConnection(url, user, password);
    }
}
