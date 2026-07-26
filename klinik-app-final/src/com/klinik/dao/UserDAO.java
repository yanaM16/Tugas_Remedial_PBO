package com.klinik.dao;

import com.klinik.model.User;
import com.klinik.util.DatabaseConnection;

import java.sql.*;

public class UserDAO {
    // Verifikasi login; mengembalikan null jika gagal (dipakai FORM LOGIN)
    public User login(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getString("username"), rs.getString("password"), rs.getString("role"));
                }
            }
        }
        return null;
    }
}
