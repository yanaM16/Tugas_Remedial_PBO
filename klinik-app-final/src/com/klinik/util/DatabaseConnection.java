package com.klinik.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Singleton sederhana untuk koneksi database (MySQL)
public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/klinik_db?useSSL=false&serverTimezone=Asia/Jakarta";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // sesuaikan dengan password MySQL Anda

    private static Connection connection;

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC MySQL tidak ditemukan. Tambahkan mysql-connector-j ke classpath.", e);
            }
        }
        return connection;
    }
}
