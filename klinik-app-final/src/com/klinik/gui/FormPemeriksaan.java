package com.klinik.gui;

import com.klinik.dao.*;
import com.klinik.exception.DataTidakDitemukanException;
import com.klinik.exception.StokTidakCukupException;
import com.klinik.model.*;
import com.klinik.util.FileLogger;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;

// FORM TRANSAKSI: input pemeriksaan pasien beserta resep obat
public class FormPemeriksaan extends JFrame {
    private JTextField txtIdPemeriksaan, txtIdPasien, txtIdDokter, txtTanggal, txtKeluhan, txtDiagnosa;
    private JTextField txtKodeObat, txtJumlah;
    private JTable tabelObat;
    private DefaultTableModel modelObat;
    private JLabel lblTotal;

    private PasienDAO pasienDAO = new PasienDAO();
    private DokterDAO dokterDAO = new DokterDAO();
    private ObatDAO obatDAO = new ObatDAO();
    private PemeriksaanDAO pemeriksaanDAO = new PemeriksaanDAO();

    private Pemeriksaan pemeriksaanAktif;

    public FormPemeriksaan() {
        setTitle("Form Transaksi / Pemeriksaan Pasien");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(3, 4, 5, 5));
        txtIdPemeriksaan = new JTextField();
        txtIdPasien = new JTextField();
        txtIdDokter = new JTextField();
        txtTanggal = new JTextField();
        txtKeluhan = new JTextField();
        txtDiagnosa = new JTextField();
        form.add(new JLabel("ID Pemeriksaan:")); form.add(txtIdPemeriksaan);
        form.add(new JLabel("ID Pasien:")); form.add(txtIdPasien);
        form.add(new JLabel("ID Dokter:")); form.add(txtIdDokter);
        form.add(new JLabel("Tanggal (yyyy-mm-dd):")); form.add(txtTanggal);
        form.add(new JLabel("Keluhan:")); form.add(txtKeluhan);
        form.add(new JLabel("Diagnosa:")); form.add(txtDiagnosa);

        JPanel panelObat = new JPanel(new BorderLayout());
        JPanel inputObat = new JPanel();
        txtKodeObat = new JTextField(10);
        txtJumlah = new JTextField(5);
        JButton btnTambahObat = new JButton("Tambah Obat ke Resep");
        inputObat.add(new JLabel("Kode Obat:")); inputObat.add(txtKodeObat);
        inputObat.add(new JLabel("Jumlah:")); inputObat.add(txtJumlah);
        inputObat.add(btnTambahObat);

        modelObat = new DefaultTableModel(new String[]{"Kode", "Nama Obat", "Jumlah", "Subtotal"}, 0);
        tabelObat = new JTable(modelObat);

        lblTotal = new JLabel("Total Biaya: Rp 0");

        panelObat.add(inputObat, BorderLayout.NORTH);
        panelObat.add(new JScrollPane(tabelObat), BorderLayout.CENTER);
        panelObat.add(lblTotal, BorderLayout.SOUTH);

        JButton btnMulai = new JButton("Mulai Pemeriksaan Baru");
        JButton btnSimpan = new JButton("Simpan Pemeriksaan");
        JPanel tombolBawah = new JPanel();
        tombolBawah.add(btnMulai);
        tombolBawah.add(btnSimpan);

        add(form, BorderLayout.NORTH);
        add(panelObat, BorderLayout.CENTER);
        add(tombolBawah, BorderLayout.SOUTH);

        btnMulai.addActionListener(e -> mulaiPemeriksaan());
        btnTambahObat.addActionListener(e -> tambahObatKeResep());
        btnSimpan.addActionListener(e -> simpanPemeriksaan());
    }

    private void mulaiPemeriksaan() {
        // EXCEPTION HANDLING: pasien/dokter tidak ditemukan ditangani rapi, tidak membuat aplikasi crash
        try {
            Pasien pasien = pasienDAO.cariById(txtIdPasien.getText());
            Dokter dokter = dokterDAO.cariById(txtIdDokter.getText());
            pemeriksaanAktif = new Pemeriksaan(txtIdPemeriksaan.getText(), pasien, dokter,
                    txtTanggal.getText(), txtKeluhan.getText(), txtDiagnosa.getText());
            modelObat.setRowCount(0);
            lblTotal.setText("Total Biaya: Rp 0");
            JOptionPane.showMessageDialog(this, "Pemeriksaan dimulai untuk pasien: " + pasien.getInfo());
        } catch (DataTidakDitemukanException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Data Tidak Ditemukan", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage());
        }
    }

    private void tambahObatKeResep() {
        if (pemeriksaanAktif == null) {
            JOptionPane.showMessageDialog(this, "Klik 'Mulai Pemeriksaan Baru' terlebih dahulu.");
            return;
        }
        try {
            int jumlah = Integer.parseInt(txtJumlah.getText());
            Obat obat = obatDAO.cariByKode(txtKodeObat.getText());

            // EXCEPTION HANDLING: cek stok sebelum ditambahkan ke resep
            obat.kurangiStok(jumlah); // akan melempar StokTidakCukupException jika stok tidak cukup
            obatDAO.updateStok(obat.getKodeObat(), obat.getStok());

            DetailObat detail = new DetailObat(obat, jumlah);
            pemeriksaanAktif.tambahObat(detail);

            modelObat.addRow(new Object[]{obat.getKodeObat(), obat.getNamaObat(), jumlah, detail.getSubtotal()});
            lblTotal.setText(String.format("Total Biaya: Rp %.0f", pemeriksaanAktif.getTotalBiaya()));

            txtKodeObat.setText(""); txtJumlah.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka.");
        } catch (DataTidakDitemukanException | StokTidakCukupException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Peringatan", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Kesalahan database: " + ex.getMessage());
        }
    }

    private void simpanPemeriksaan() {
        if (pemeriksaanAktif == null) {
            JOptionPane.showMessageDialog(this, "Belum ada pemeriksaan yang dimulai.");
            return;
        }
        try {
            pemeriksaanDAO.simpan(pemeriksaanAktif);
            FileLogger.log("Pemeriksaan " + pemeriksaanAktif.getIdPemeriksaan() + " disimpan. Total: " + pemeriksaanAktif.getTotalBiaya());
            JOptionPane.showMessageDialog(this, "Pemeriksaan berhasil disimpan.");
            pemeriksaanAktif = null;
            modelObat.setRowCount(0);
        } catch (SQLException ex) {
            FileLogger.log("ERROR simpan pemeriksaan: " + ex.getMessage());
            JOptionPane.showMessageDialog(this, "Gagal menyimpan pemeriksaan: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
