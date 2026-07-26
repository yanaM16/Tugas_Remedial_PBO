package com.klinik.gui;

import com.klinik.model.User;

import javax.swing.*;
import java.awt.*;

public class MenuUtama extends JFrame {
    private User userLogin;

    public MenuUtama(User user) {
        this.userLogin = user;
        setTitle("Menu Utama - Sistem Klinik Sederhana (Login sebagai: " + user.getRole() + ")");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 1, 10, 10));

        JButton btnPasien = new JButton("Data Master Pasien");
        JButton btnDokter = new JButton("Data Master Dokter");
        JButton btnObat = new JButton("Data Master Obat");
        JButton btnTransaksi = new JButton("Form Transaksi / Pemeriksaan");
        JButton btnLaporan = new JButton("Laporan");

        btnPasien.addActionListener(e -> new FormPasien().setVisible(true));
        btnDokter.addActionListener(e -> new FormDokter().setVisible(true));
        btnObat.addActionListener(e -> new FormObat().setVisible(true));
        btnTransaksi.addActionListener(e -> new FormPemeriksaan().setVisible(true));
        btnLaporan.addActionListener(e -> new FormLaporan().setVisible(true));

        add(btnPasien);
        add(btnDokter);
        add(btnObat);
        add(btnTransaksi);
        add(btnLaporan);
    }
}
