package com.klinik.gui;

import com.klinik.dao.PasienDAO;
import com.klinik.model.Pasien;
import com.klinik.util.Validasi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

// FORM DATA MASTER: contoh CRUD lengkap + pencarian untuk entitas Pasien
public class FormPasien extends JFrame {
    private JTextField txtId, txtNoRM, txtNama, txtAlamat, txtTelp, txtTglLahir, txtCari;
    private JComboBox<String> cbJK;
    private JTable table;
    private DefaultTableModel model;
    private PasienDAO dao = new PasienDAO();

    public FormPasien() {
        setTitle("Data Master Pasien");
        setSize(750, 500);
        setLocationRelativeTo(null);

        JPanel form = new JPanel(new GridLayout(4, 4, 5, 5));
        txtId = new JTextField(); txtNoRM = new JTextField();
        txtNama = new JTextField(); txtAlamat = new JTextField();
        txtTelp = new JTextField(); txtTglLahir = new JTextField();
        cbJK = new JComboBox<>(new String[]{"L", "P"});

        form.add(new JLabel("ID Pasien:")); form.add(txtId);
        form.add(new JLabel("No. RM:")); form.add(txtNoRM);
        form.add(new JLabel("Nama:")); form.add(txtNama);
        form.add(new JLabel("Alamat:")); form.add(txtAlamat);
        form.add(new JLabel("No. Telp:")); form.add(txtTelp);
        form.add(new JLabel("Tgl Lahir (yyyy-mm-dd):")); form.add(txtTglLahir);
        form.add(new JLabel("Jenis Kelamin:")); form.add(cbJK);

        JPanel tombol = new JPanel();
        JButton btnTambah = new JButton("Tambah");
        JButton btnUbah = new JButton("Ubah");
        JButton btnHapus = new JButton("Hapus");
        JButton btnBersih = new JButton("Bersihkan");
        tombol.add(btnTambah); tombol.add(btnUbah); tombol.add(btnHapus); tombol.add(btnBersih);

        JPanel cariPanel = new JPanel(new BorderLayout());
        txtCari = new JTextField();
        JButton btnCari = new JButton("Cari");
        cariPanel.add(new JLabel("Cari (nama/no RM): "), BorderLayout.WEST);
        cariPanel.add(txtCari, BorderLayout.CENTER);
        cariPanel.add(btnCari, BorderLayout.EAST);

        model = new DefaultTableModel(new String[]{"ID", "No RM", "Nama", "Alamat", "Telp", "Tgl Lahir", "JK"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);

        JPanel atas = new JPanel(new BorderLayout());
        atas.add(form, BorderLayout.CENTER);
        atas.add(tombol, BorderLayout.SOUTH);

        setLayout(new BorderLayout());
        add(atas, BorderLayout.NORTH);
        add(cariPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        // susun ulang layout agar tabel terlihat jelas
        getContentPane().removeAll();
        setLayout(new BorderLayout());
        JPanel utara = new JPanel(new BorderLayout());
        utara.add(atas, BorderLayout.NORTH);
        utara.add(cariPanel, BorderLayout.SOUTH);
        add(utara, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        btnTambah.addActionListener(e -> tambahData());
        btnUbah.addActionListener(e -> ubahData());
        btnHapus.addActionListener(e -> hapusData());
        btnBersih.addActionListener(e -> bersihkanForm());
        btnCari.addActionListener(e -> cariData());

        table.getSelectionModel().addListSelectionListener(e -> isiFormDariTabel());

        muatSemuaData();
    }

    private void tambahData() {
        if (Validasi.isKosong(txtId.getText()) || Validasi.isKosong(txtNama.getText())) {
            JOptionPane.showMessageDialog(this, "ID dan Nama wajib diisi!");
            return;
        }
        if (!Validasi.isNoTelpValid(txtTelp.getText())) {
            JOptionPane.showMessageDialog(this, "No. Telp tidak valid (harus 9-13 digit angka).");
            return;
        }
        try {
            Pasien p = new Pasien(txtId.getText(), txtNama.getText(), txtAlamat.getText(),
                    txtTelp.getText(), txtNoRM.getText(), txtTglLahir.getText(), (String) cbJK.getSelectedItem());
            dao.tambah(p);
            JOptionPane.showMessageDialog(this, "Data pasien berhasil ditambahkan.");
            muatSemuaData();
            bersihkanForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ubahData() {
        try {
            Pasien p = new Pasien(txtId.getText(), txtNama.getText(), txtAlamat.getText(),
                    txtTelp.getText(), txtNoRM.getText(), txtTglLahir.getText(), (String) cbJK.getSelectedItem());
            dao.ubah(p);
            JOptionPane.showMessageDialog(this, "Data pasien berhasil diubah.");
            muatSemuaData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal mengubah: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusData() {
        if (Validasi.isKosong(txtId.getText())) return;
        int konfirmasi = JOptionPane.showConfirmDialog(this, "Hapus data pasien ini?");
        if (konfirmasi != JOptionPane.YES_OPTION) return;
        try {
            dao.hapus(txtId.getText());
            JOptionPane.showMessageDialog(this, "Data pasien berhasil dihapus.");
            muatSemuaData();
            bersihkanForm();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cariData() {
        try {
            List<Pasien> hasil = dao.cari(txtCari.getText());
            tampilkanKeTabel(hasil);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal mencari: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void muatSemuaData() {
        try {
            tampilkanKeTabel(dao.semua());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void tampilkanKeTabel(List<Pasien> daftar) {
        model.setRowCount(0);
        for (Pasien p : daftar) {
            model.addRow(new Object[]{p.getId(), p.getNoRM(), p.getNama(), p.getAlamat(),
                    p.getNoTelp(), p.getTanggalLahir(), p.getJenisKelamin()});
        }
    }

    private void isiFormDariTabel() {
        int row = table.getSelectedRow();
        if (row < 0) return;
        txtId.setText(model.getValueAt(row, 0).toString());
        txtNoRM.setText(model.getValueAt(row, 1).toString());
        txtNama.setText(model.getValueAt(row, 2).toString());
        txtAlamat.setText(model.getValueAt(row, 3).toString());
        txtTelp.setText(model.getValueAt(row, 4).toString());
        txtTglLahir.setText(model.getValueAt(row, 5).toString());
        cbJK.setSelectedItem(model.getValueAt(row, 6).toString());
    }

    private void bersihkanForm() {
        txtId.setText(""); txtNoRM.setText(""); txtNama.setText("");
        txtAlamat.setText(""); txtTelp.setText(""); txtTglLahir.setText("");
    }
}
