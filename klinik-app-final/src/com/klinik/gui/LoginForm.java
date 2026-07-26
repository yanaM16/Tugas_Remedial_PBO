package com.klinik.gui;

import com.klinik.dao.UserDAO;
import com.klinik.model.User;
import com.klinik.util.FileLogger;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;

    public LoginForm() {
        setTitle("Login - Sistem Klinik Sederhana");
        setSize(350, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        add(txtPassword, gbc);

        JButton btnLogin = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        add(btnLogin, gbc);

        btnLogin.addActionListener(e -> lakukanLogin());
    }

    private void lakukanLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        // EXCEPTION HANDLING: menangani error koneksi database saat login
        try {
            UserDAO dao = new UserDAO();
            User user = dao.login(username, password);
            if (user != null) {
                FileLogger.log("Login berhasil: " + username);
                JOptionPane.showMessageDialog(this, "Selamat datang, " + user.getUsername() + " (" + user.getRole() + ")");
                new MenuUtama(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Username atau password salah!", "Gagal Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            FileLogger.log("ERROR koneksi database saat login: " + ex.getMessage());
            JOptionPane.showMessageDialog(this,
                "Tidak dapat terhubung ke database.\n" + ex.getMessage(),
                "Kesalahan Database", JOptionPane.ERROR_MESSAGE);
        }
    }
}
