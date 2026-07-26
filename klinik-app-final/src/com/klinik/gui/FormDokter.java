package com.klinik.gui;

import com.klinik.dao.DokterDAO;
import com.klinik.model.Dokter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class FormDokter extends JFrame {
    private JTextField txtId, txtNoSIP, txtNama, txtAlamat, txtTelp, txtSpesialisasi, txtCari;
    private JTable table;
    private DefaultTableModel model;
    private DokterDAO dao = new DokterDAO();

    public FormDokter() {
        setTitle("Data Master Dokter");
        setSize(750, 480);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(3, 4, 5, 5));
        txtId = new JTextField(); txtNoSIP = new JTextField();
        txtNama = new JTextField(); txtAlamat = new JTextField();
        txtTelp = new JTextField(); txtSpesialisasi = new JTextField();
        form.add(new JLabel("ID Dokter:")); form.add(txtId);
        form.add(new JLabel("No SIP:")); form.add(txtNoSIP);
        form.add(new JLabel("Nama:")); form.add(txtNama);
        form.add(new JLabel("Alamat:")); form.add(txtAlamat);
        form.add(new JLabel("No Telp:")); form.add(txtTelp);
        form.add(new JLabel("Spesialisasi:")); form.add(txtSpesialisasi);

        JPanel tombol = new JPanel();
        JButton btnTambah = new JButton("Tambah"), btnUbah = new JButton("Ubah"),
                btnHapus = new JButton("Hapus"), btnBersih = new JButton("Bersihkan");
        tombol.add(btnTambah); tombol.add(btnUbah); tombol.add(btnHapus); tombol.add(btnBersih);

        JPanel cariPanel = new JPanel(new BorderLayout());
        txtCari = new JTextField();
        JButton btnCari = new JButton("Cari");
        cariPanel.add(new JLabel("Cari (nama/spesialisasi): "), BorderLayout.WEST);
        cariPanel.add(txtCari, BorderLayout.CENTER);
        cariPanel.add(btnCari, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"ID", "No SIP", "Nama", "Alamat", "Telp", "Spesialisasi"}, 0);
        table = new JTable(model);

        JPanel utara = new JPanel(new BorderLayout());
        JPanel atas = new JPanel(new BorderLayout());
        atas.add(form, BorderLayout.CENTER);
        atas.add(tombol, BorderLayout.SOUTH);
        utara.add(atas, BorderLayout.NORTH);
        utara.add(cariPanel, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(utara, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambah());
        btnUbah.addActionListener(e -> ubah());
        btnHapus.addActionListener(e -> hapus());
        btnBersih.addActionListener(e -> bersih());
        btnCari.addActionListener(e -> cari());
        table.getSelectionModel().addListSelectionListener(e -> isiForm());

        muat();
    }

    private void tambah() {
        try {
            dao.tambah(new Dokter(txtId.getText(), txtNama.getText(), txtAlamat.getText(),
                    txtTelp.getText(), txtNoSIP.getText(), txtSpesialisasi.getText()));
            JOptionPane.showMessageDialog(this, "Data dokter ditambahkan.");
            muat(); bersih();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void ubah() {
        try {
            dao.ubah(new Dokter(txtId.getText(), txtNama.getText(), txtAlamat.getText(),
                    txtTelp.getText(), txtNoSIP.getText(), txtSpesialisasi.getText()));
            JOptionPane.showMessageDialog(this, "Data dokter diubah.");
            muat();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void hapus() {
        try {
            dao.hapus(txtId.getText());
            muat(); bersih();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void cari() {
        try {
            tampilkan(dao.cari(txtCari.getText()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal: " + ex.getMessage());
        }
    }

    private void muat() {
        try {
            tampilkan(dao.semua());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage());
        }
    }

    private void tampilkan(List<Dokter> daftar) {
        model.setRowCount(0);
        for (Dokter d : daftar) {
            model.addRow(new Object[]{d.getId(), d.getNoSIP(), d.getNama(), d.getAlamat(), d.getNoTelp(), d.getSpesialisasi()});
        }
    }

    private void isiForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtId.setText(model.getValueAt(row, 0).toString());
        txtNoSIP.setText(model.getValueAt(row, 1).toString());
        txtNama.setText(model.getValueAt(row, 2).toString());
        txtAlamat.setText(model.getValueAt(row, 3).toString());
        txtTelp.setText(model.getValueAt(row, 4).toString());
        txtSpesialisasi.setText(model.getValueAt(row, 5).toString());
    }

    private void bersih() {
        txtId.setText(""); txtNoSIP.setText(""); txtNama.setText("");
        txtAlamat.setText(""); txtTelp.setText(""); txtSpesialisasi.setText("");
    }
}
